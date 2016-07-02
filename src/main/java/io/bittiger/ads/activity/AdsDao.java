package io.bittiger.ads.activity;

import io.bittiger.ads.util.Ad;
import net.spy.memcached.MemcachedClient;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.FileReader;
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

    public void testMemcached() throws IOException {

        String someObject = "Some Object";

        getCache().set("someKey", MEMCACHED_EXPIRATION_TIME, someObject);

        Object object = getCache().get("someKey");

        System.out.println(object);
    }

    public void shutdown() {
        cache.shutdown();
    }

    public boolean loadLogfile() throws IOException {
        /* System.getProperty(USER_DIR) + ADS_LOCATION cannot is not file path
        readFile path should set to your own path,
        for example:
        /Users/sleephu2/Dropbox/GitRepository/ads-searching-system" + ADS_LOCATION
        */
        String jsonData = readFile(System.getProperty(USER_DIR) + ADS_LOCATION);
        System.out.println(System.getProperty(USER_DIR));
        JSONArray jsonArr = new JSONArray(jsonData);

        for (int i = 0; i < jsonArr.length(); i++) {
            Ad ad = new Ad();
            ad.setAdId(jsonArr.getJSONObject(i).getLong(AD_ID));
            ad.setCampaignId(jsonArr.getJSONObject(i).getLong(CAMPAIGN_ID));
            ad.setKeywords(QueryUnderstanding.getInstance().parseQuery(jsonArr.getJSONObject(i).getString(KEYWORDS)));
            ad.setBid(jsonArr.getJSONObject(i).getDouble(BID));
            ad.setpClick(jsonArr.getJSONObject(i).getDouble(PCLICK));

            setAd(ad);
        }
        return true;
    }

    private String readFile(String filename) {
        String result = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            result = sb.toString();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return result;
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
                    getCache().replace(invKey, 3600, ads);
                } else {
                    /****** Add one ad to inv index when invKey does not exist. 
                     * This will be moved to AdsSelection after we find one way
                     * to traverse the fwdKey in spymemcached ******/

                    ads = new HashSet<Ad>();
                    ads.add(ad);
                    getCache().set(invKey, 3600, ads);
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
