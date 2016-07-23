package io.bittiger.ads.datastore;

import io.bittiger.ads.util.Ad;
import io.bittiger.ads.util.Campaign;
import net.spy.memcached.MemcachedClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

import static io.bittiger.ads.util.Config.*;

public class AdsIndex {
    private static AdsIndex instance = null;

    protected AdsIndex() {
    }

    public static AdsIndex getInstance() {
        if (instance == null) {
            instance = new AdsIndex();
        }
        return instance;
    }

    private static MemcachedClient cache = null;

    private MemcachedClient getCache() throws IOException {
        if (cache == null) {
            String env = System.getProperty(USER_DIR);
            System.out.println("User dir: "+ env);
            String hostname;
            int port;

            if (env.length() - env.lastIndexOf("/") > 21){
                hostname = HEROKU_MEMCACHED_HOST_NAME;
                port = HEROKU_MEMCACHED_PORT;
            } else {
                hostname = MEMCACHED_HOST_NAME;
                port = MEMCACHED_PORT;
            }
            cache = new MemcachedClient(new InetSocketAddress(hostname, port));
        }
        return cache;
    }

    public void shutdown() {
        cache.shutdown();
    }


    /******
     * Inverted Index
     ******/
    public Set<Ad> getInvertedIndex(String keyword) {
        Set<Ad> curr = new HashSet<Ad>();
        Set<Long> index = getInvertedIndexFromCache(keyword);

        if (index == null) {
            curr = AdsDao.getInstance().traverseAds(keyword);
        } else {
            for (Long adId : index) {
                curr.add(getAd(adId));
            }
        }
        return curr;
    }

    private Set<Long> getInvertedIndexFromCache(String keyword) {
        try {
            String invKey = "inv" + keyword;
            return (Set<Long>) getCache().get(invKey);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /******
     * Forward Index
     ******/
    public Ad getAd(long key) {
        try {
            Ad ad = (Ad)getCache().get("fwd" + Long.toString(key));
            if (ad == null) {
                ad = AdsDao.getInstance().getAdFromMongo(key);
                getCache().set("fwd" + Long.toString(key), MEMCACHED_EXPIRATION_TIME, ad);
            }
            return ad;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean setAdToCache(Ad ad) {
        /****** Add one ad to inv index if invKey exist ******/
        try {
            String[] keywords = ad.getKeywords();
            for (String keyword : keywords) {

                String invKey = "inv" + keyword;
                Set<Long> invertedIndex = (Set<Long>) getCache().get(invKey);

                if (invertedIndex != null) {
                    addOneAdIntoInvertedIndex(invertedIndex, ad.getAdId());
                    getCache().replace(invKey, MEMCACHED_EXPIRATION_TIME, invertedIndex);
                } else {
                    invertedIndex = new HashSet<Long>();
                    invertedIndex.add(ad.getAdId());
                    getCache().set(invKey, MEMCACHED_EXPIRATION_TIME, invertedIndex);
                }
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void addOneAdIntoInvertedIndex(Set<Long> invertedIndex, long newAdId) {
        if (!invertedIndex.contains(newAdId)) {
            invertedIndex.add(newAdId);
        }
    }

    private boolean setCampaignToCache(Campaign campaign) {
        /****** Add one ad to inv index if invKey exist ******/
        try {
            String campaignKey = "cmp" + Long.toString(campaign.getCampaignId());
            Campaign existingCampaign = (Campaign) getCache().get(campaignKey);

            if (existingCampaign != null) {
                getCache().replace(campaignKey, MEMCACHED_EXPIRATION_TIME, campaign);
            } else {
                getCache().set(campaignKey, MEMCACHED_EXPIRATION_TIME, campaign);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Campaign getCampaignFromCache(long campaignId) {
        try {

            return  (Campaign) getCache().get("cmp" + Long.toString(campaignId));

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Campaign getCampaign(long campaignId) {
        Campaign campaign = getCampaignFromCache(campaignId);

        if (campaign == null) {
            campaign = AdsDao.getInstance().getCampaign(campaignId);
            setCampaignToCache(campaign);
        }
        return campaign;
    }

    public boolean setCampaign(Campaign campaign) {
        return setCampaignToCache(campaign) && AdsDao.getInstance().setCampaign(campaign);
    }
}
