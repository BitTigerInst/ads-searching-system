package io.bittiger.ads.activity;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import io.bittiger.ads.util.Ad;
import net.spy.memcached.MemcachedClient;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static io.bittiger.ads.util.Config.*;

public class AdsDao {
    private static AdsDao instance = null;
    private static MemcachedClient cache = null;
    private static MongoClient mongo = null;

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

    private MongoClient getMongo() throws IOException {
        if (mongo == null) {
            mongo = new MongoClient(MONGODB_HOST_NAME, MONGODB_PORT);
        }
        return mongo;
    }

    private DBCollection getAdsCollection() {
        try {
            DB mongoDatabase = getMongo().getDB(ADS_DB);
            System.out.println("Connect to database successfully");
            DBCollection collection = mongoDatabase.getCollection(ADS_COLLECTION);
            System.out.println("Collection chosen successfully");
            return collection;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void shutdown() {
        cache.shutdown();
    }

    public boolean loadLogfile() throws IOException {
        /* System.getProperty(USER_DIR) + ADS_LOCATION cannot is not file path
        readFile path should set to your own path,
        for example:
        "/Users/sleephu2/Dropbox/GitRepository/ads-searching-system" + ADS_LOCATION
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /******
     * Inverted Index
     ******/
    public Set<Ad> getAds(String key) {
        try {
            return (Set<Ad>) getCache().get(key);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /******
     * Forward Index
     ******/
    public Ad getAd(long key) {
        Set<Ad> ads = new HashSet<Ad>();
        DBCollection collection = getAdsCollection();
        DBCursor cursor = collection.find(new BasicDBObject(AD_ID, key));
        while (cursor.hasNext()) {
            DBObject theObj = cursor.next();
            Long adId = (Long) (theObj.get(AD_ID));
            Long campaignId = (Long) theObj.get(CAMPAIGN_ID);
            String keywordsArray = (String) theObj.get(KEYWORDS);
            String[] keywords = keywordsArray.split(" ");
            Double bid = (Double) theObj.get(BID);
            Double pClick = (Double) theObj.get(PCLICK);

            Ad ad = new Ad();
            ad.setAdId(adId);
            ad.setCampaignId(campaignId);
            ad.setKeywords(keywords);
            ad.setBid(bid);
            ad.setpClick(pClick);

            addOneAd(ads, ad);
//            }
        }
        return new ArrayList<Ad>(ads).get(0);
    }

    public Set<Ad> traverseFwdIndex(String keyword) {
        Set<Ad> ads = new HashSet<Ad>();
        DBCollection collection = getAdsCollection();
        DBCursor cursor = collection.find();
        while (cursor.hasNext()) {

            DBObject adObj = cursor.next();
            String keywords_str = (String) adObj.get(KEYWORDS);

            if (keywords_str.contains(keyword)) {
                Long adId = (Long) (adObj.get(AD_ID));
                Long campaignId = (Long) adObj.get(CAMPAIGN_ID);
                String[] keywords = keywords_str.split(" ");
                Double bid = (Double) adObj.get(BID);
                Double pClick = (Double) adObj.get(PCLICK);

                Ad ad = new Ad();
                ad.setAdId(adId);
                ad.setCampaignId(campaignId);
                ad.setKeywords(keywords);
                ad.setBid(bid);
                ad.setpClick(pClick);

                addOneAd(ads, ad);
            }
        }
        return ads;
    }

    public boolean setAd(Ad ad) {
        try {
            /****** Add one single ad to fwd index ******/
            setAdToMongo(ad);

            /****** Add one ad to inv index if invKey exist ******/
            String[] keywords = ad.getKeywords();
            for (String keyword : keywords) {
                String invKey = "inv" + keyword;
                Set<Ad> ads = (Set<Ad>) getCache().get(invKey);
                if (ads != null) {
                    addOneAd(ads, ad);
                    getCache().replace(invKey, MEMCACHED_EXPIRATION_TIME, ads);
                } else {
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

    private void setAdToMongo(Ad ad) {
        try {
            DBCollection collection = getAdsCollection();
            StringBuilder strKeyword = new StringBuilder();
            for (String keyword : ad.getKeywords()) {
                strKeyword.append(keyword).append(' ');
            }
            BasicDBObject doc = new BasicDBObject(AD_ID, ad.getAdId()).
                    append(CAMPAIGN_ID, ad.getCampaignId()).
                    append(KEYWORDS, strKeyword.substring(0, strKeyword.length() - 1)).
                    append(BID, ad.getBid()).
                    append(PCLICK, ad.getpClick());

            collection.insert(doc);
            System.out.println("One Ad is inserted successfully");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
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