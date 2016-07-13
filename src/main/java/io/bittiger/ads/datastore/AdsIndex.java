package io.bittiger.ads.datastore;

import io.bittiger.ads.activity.AdsDao;
import io.bittiger.ads.util.Ad;
import io.bittiger.ads.util.Campaign;
import net.spy.memcached.MemcachedClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashSet;
import java.util.Set;

import static io.bittiger.ads.util.Config.MEMCACHED_EXPIRATION_TIME;
import static io.bittiger.ads.util.Config.MEMCACHED_HOST_NAME;
import static io.bittiger.ads.util.Config.MEMCACHED_PORT;

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

    public MemcachedClient getCache() throws IOException {
        if (cache == null) {
            cache = new MemcachedClient(new InetSocketAddress(MEMCACHED_HOST_NAME, MEMCACHED_PORT));
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
        Set<Ad> curr = getInvertedIndexFromCache(keyword);
        if (curr == null) {
            curr = AdsDao.getInstance().traverseAds(keyword);
        }
        return curr;
    }

    private Set<Ad> getInvertedIndexFromCache(String keyword) {
        try {
            String invKey = "inv" + keyword;
            return (Set<Ad>) getCache().get(invKey);
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
                Set<Ad> ads = (Set<Ad>) getCache().get(invKey);

                if (ads != null) {
                    addOneAdIntoInvertedIndex(ads, ad);
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

    private void addOneAdIntoInvertedIndex(Set<Ad> ads, Ad newAd) {
        long newId = newAd.getAdId();
        for (Ad ad : ads) {
            if (ad.getAdId() == newId) {
                return;
            }
        }
        ads.add(newAd);
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
