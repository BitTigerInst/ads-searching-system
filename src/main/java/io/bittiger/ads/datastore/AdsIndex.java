package io.bittiger.ads.datastore;

import io.bittiger.ads.util.Ad;
import io.bittiger.ads.util.Campaign;
import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactory;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.auth.AuthDescriptor;
import net.spy.memcached.auth.PlainCallbackHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static io.bittiger.ads.util.Config.*;

public class AdsIndex {
    private static AdsIndex instance = null;
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock writeLock = lock.writeLock();
    private final Lock readLock = lock.readLock();

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
            if (System.getProperty(USER_DIR).equals("/app")) {
                AuthDescriptor ad = new AuthDescriptor(new String[]{"PLAIN"},
                        new PlainCallbackHandler(MEMCACHED_USERNAME, MEMCACHED_PASSWORD));
                ConnectionFactoryBuilder factoryBuilder = new ConnectionFactoryBuilder();
                ConnectionFactory cf = factoryBuilder.setProtocol(ConnectionFactoryBuilder.Protocol.BINARY)
                                                     .setAuthDescriptor(ad)
                                                     .build();
                cache = new MemcachedClient(cf,
                            Collections.singletonList(new InetSocketAddress(HEROKU_MEMCACHED_HOST_NAME,
                                                                            HEROKU_MEMCACHED_PORT)));
            } else {
                cache = new MemcachedClient(new InetSocketAddress(MEMCACHED_HOST_NAME, MEMCACHED_PORT));
            }

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
        readLock.lock();
        try {
            Campaign campaign = getCampaignFromCache(campaignId);
            if (campaign == null) {
                campaign = AdsDao.getInstance().getCampaign(campaignId);
                setCampaignToCache(campaign);
            }
            return campaign;
        } finally {
            readLock.unlock();
        }
    }

    public boolean setCampaign(Campaign campaign) {
        writeLock.lock();
        try {
            return setCampaignToCache(campaign) && AdsDao.getInstance().setCampaign(campaign);
        } finally {
            writeLock.unlock();
        }
    }
}
