package io.bittiger.ads.activity;

import io.bittiger.ads.util.Ad;
import net.spy.memcached.MemcachedClient;

import java.util.Set;
import java.util.HashSet;

import java.io.IOException;
import java.net.InetSocketAddress;

import static io.bittiger.ads.util.Config.*;

public class AdsDao {
    private static AdsDao instance = null;
    private static MemcachedClient cache = null;

    protected AdsDao() {
    }

    public static AdsDao getInstance() {
        if (instance == null) {
            instance = new AdsDao();
        }
        return instance;
    }

    private MemcachedClient getCache() throws IOException {
        if (cache == null) {
            cache = new MemcachedClient(new InetSocketAddress(MEMCACHED_HOST_NAME, MEMCACHED_PORT));
        }
        return cache;
    }

    public void shutdown() {
        cache.shutdown();
    }

    public void testMemcached() throws IOException {

        String someObject = "Some Object";

//        getCache().set("someKey", MEMCACHED_EXPIRATION_TIME, someObject);

        Object object = getCache().get("someKey");

        System.out.println(object);

//        getCache().delete("someKey");
    }


    /****** Inverted Index ******/
    public Set<Ad> getAds(String key) {
        try {
            return (Set<Ad>) getCache().get(key);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /****** Forward Index ******/
    public Ad getAd(long key) {
        try {
            Ad ad = (Ad)getCache().get("fwd" + Long.toString(key));
            return ad;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Set<Ad> traverseFwdIndex(String keyword) {
        Set<Ad> ads = new HashSet<Ad>();
            /* do nothing for now
            since spymemcached does not support traversing all the keys,
            this method will be used when we setup an independent
            database/HashMap which supports traversing keys */
        return ads;
    }

    public boolean setAd(Ad ad) {
        String key = Long.toString(ad.getAdId());
        String fwdKey = "fwd" + key;

        try {
            /****** Add one single ad to fwd index ******/
            getCache().set(fwdKey, MEMCACHED_EXPIRATION_TIME, ad);

            /****** Add one ad to inv index if invKey exist ******/
            String[] keywords = ad.getKeywords();
            for (String keyword : keywords) {
                String invKey = "inv" + keyword;
                Set<Ad> ads = (Set<Ad>) getCache().get(invKey);
                if (ads != null) {
                    addOneAd(ads, ad);
                    getCache().replace(invKey, MEMCACHED_EXPIRATION_TIME, ads);
                } else {
                    /****** Add one ad to inv index when invKey does not exist. 
                     * This will be moved to AdsSelection after we find one way
                     * to traverse the fwdKey in spymemcached ******/

                    ads = new HashSet<Ad>();
                    ads.add(ad);
                    getCache().set(invKey, MEMCACHED_EXPIRATION_TIME, ads);
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void addOneAd(Set<Ad> ads, Ad newAd) {
        long newId = newAd.getAdId();
        for (Ad ad : ads) {
            if (ad.getAdId() == newId) {
                return;
            }
        }
        ads.add(newAd);
    }
}
