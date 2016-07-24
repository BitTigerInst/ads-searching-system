package io.bittiger.ads.datastore;

import com.mongodb.*;
import com.mongodb.client.MongoDatabase;
import io.bittiger.ads.util.Ad;
import io.bittiger.ads.util.Campaign;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static io.bittiger.ads.util.Config.*;

public class AdsDao {
    private static AdsDao instance = null;
    private static MongoClient mongoClient = null;
    private static MongoClientURI mongoClientURI = null;

    protected AdsDao() {
    }

    public static AdsDao getInstance() {
        if (instance == null) {
            instance = new AdsDao();
        }
        return instance;
    }

    private MongoClient getMongo() throws IOException {
        if (mongoClientURI == null) {
            if (System.getProperty(USER_DIR).equals("/app")) {
                mongoClientURI = new MongoClientURI(HEROKU_MONGODB_HOST_NAME+":"+HEROKU_MONGODB_PORT+"/"+ADS_DB);
                mongoClient = new MongoClient(mongoClientURI);
            } else {
                mongoClient = new MongoClient(MONGODB_HOST_NAME, MONGODB_PORT);
            }
        }
        return mongoClient;
    }

    private DBCollection getAdsCollection() {
        try {
            DB mongoDatabase = getMongo().getDB(ADS_DB);
            System.out.println("Connect to database successfully");
            DBCollection collection = mongoDatabase.getCollection(ADS_COLLECTION);
            System.out.println("Collection Ads chosen successfully");
            return collection;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private DBCollection getCampaignsCollection() {
        try {
            DB mongoDatabase = getMongo().getDB(ADS_DB);
            System.out.println("Connect to database successfully");
            DBCollection collection = mongoDatabase.getCollection(CAMPAIGNS_COLLECTION);
            System.out.println("Collection Campaigns chosen successfully");
            return collection;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void shutdown() {
        mongoClient.close();
    }


    /******
     * Forward Index
     ******/
    public Ad getAdFromMongo(long key) {
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
        }
        return new ArrayList<Ad>(ads).get(0);
    }

    public Set<Ad> traverseAds(String keyword) {
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

    public boolean setAdToMongo(Ad ad) {
        /****** Add one single ad to fwd index ******/
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

            DBCursor cursor = collection.find(doc);
            if (cursor.hasNext()) {
                BasicDBObject existing = new BasicDBObject(AD_ID, ad.getAdId());
                collection.remove(existing);
            }

            WriteResult writeResult = collection.insert(doc);
            System.out.println(writeResult.wasAcknowledged());
            System.out.println("One Ad is inserted successfully");
            return true;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
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

    public Campaign getCampaign(long key) {
        DBCollection collection = getCampaignsCollection();

        DBCursor cursor = collection.find(new BasicDBObject(CAMPAIGN_ID, key));
        if (cursor.hasNext()) {
            DBObject theObj = cursor.next();
            Long campaignId = (Long) theObj.get(CAMPAIGN_ID);
            Double budget = (Double) theObj.get(BUDGET);

            Campaign campaign = new Campaign();
            campaign.setCampaignId(campaignId);
            campaign.setBudget(budget);

            return campaign;
        }
        return null;
    }

    public boolean setCampaign(Campaign campaign) {
        /****** Add one single campaign to fwd index ******/
        try {
            DBCollection collection = getCampaignsCollection();

            BasicDBObject existing = new BasicDBObject(CAMPAIGN_ID, campaign.getCampaignId());

            DBCursor cursor = collection.find(existing);
            if (cursor.hasNext()) {
                existing = new BasicDBObject(CAMPAIGN_ID, campaign.getCampaignId());
                collection.remove(existing);
            }

            BasicDBObject doc = new BasicDBObject(CAMPAIGN_ID, campaign.getCampaignId())
                    .append(BUDGET, campaign.getBudget());
            WriteResult writeResult = collection.insert(doc);
            System.out.println(writeResult.wasAcknowledged());
            System.out.println("One Campaign is inserted successfully");
            return true;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }
}
