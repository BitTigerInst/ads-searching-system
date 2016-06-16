package io.bittiger.ads.activity;

import io.bittiger.ads.model.Ad;
import net.spy.memcached.MemcachedClient;

import java.util.Set;
import java.util.HashSet;

import java.io.IOException;
import java.net.InetSocketAddress;

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

    private static MemcachedClient getCache() throws IOException {
        if (cache == null) {
            cache = new MemcachedClient(new InetSocketAddress("127.0.0.1", 11211));
        }
        return cache;
    }

    public void testMemcached() throws IOException {


        String someObject = "Some Object";

        getCache().set("someKey", 3600, someObject);

        Object object = getCache().get("someKey");

        System.out.println(object);
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
            return (Ad)getCache().get(Long.toString(key));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean setAd(Ad ad) {
        String key = Long.toString(ad.getAdId());
        String fwdKey = "fwd" + key;

        try {
            MemcachedClient cache = getCache();
            
            /****** Add one single ad to fwd index ******/
            cache.set(fwdKey, 3600, ad);

            /****** Add one ad to inv index if invKey exist ******/
            String[] keywords = ad.getKeywords();
            for (String keyword : keywords) {
                String invKey = "inv" + keyword;
                Set<Ad> ads = (Set<Ad>) cache.get(invKey);
                if (ads != null) {
                    ads.add(ad);
                } else {
                    /****** Add one ad to inv index when invKey does not exist. 
                     * This will be moved to AdsSelect.getInstance().traverseFwdIndex() 
                     * after we find a way to traverse the fwdKey in spymemcached******/

                    ads = new HashSet<Ad>();
                    ads.add(ad);
                    cache.set(invKey, 3600, ads);
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
