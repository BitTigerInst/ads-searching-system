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
   //     /*
        if (cache == null) {
            AuthDescriptor ad = new AuthDescriptor(new String[]{"PLAIN"},
                    new PlainCallbackHandler(MEMCACHED_USERNAME, MEMCACHED_PASSWORD));
            ConnectionFactoryBuilder factoryBuilder = new ConnectionFactoryBuilder();
            ConnectionFactory cf = factoryBuilder.setProtocol(ConnectionFactoryBuilder.Protocol.BINARY).setAuthDescriptor(ad).build();
            //  cache = new MemcachedClient(new InetSocketAddress(HEROKU_MEMCACHED_HOST_NAME, HEROKU_MEMCACHED_PORT));
            cache = new MemcachedClient(cf, Collections.singletonList(new InetSocketAddress(HEROKU_MEMCACHED_HOST_NAME, HEROKU_MEMCACHED_PORT)));
        }
    //    */
        /*
        try {
            ConnectionFactory c;
            // allow auth to be disabled for local development
            if (System.getenv("MEMCACHE_NOAUTH") == null) {
                System.out.println("Using authentication with memcache");
                AuthDescriptor ad = new AuthDescriptor(
                        new String[] { "PLAIN" },
                        new PlainCallbackHandler(System.getenv(MEMCACHED_USERNAME),
                                System.getenv(MEMCACHED_PASSWORD)));
                c = new ConnectionFactoryBuilder().setProtocol(
                        ConnectionFactoryBuilder.Protocol.BINARY)
                        .setAuthDescriptor(ad).build();
            } else {
                System.out.println("Not using authentication with memcache");
                c = new ConnectionFactoryBuilder().setProtocol(
                        ConnectionFactoryBuilder.Protocol.BINARY).build();
            }
            cache = new MemcachedClient(c,
                    AddrUtil.getAddresses(MEMCACHED_HOST_NAME));
        } catch (Exception ex) {
            System.err.println(
                    "Couldn't create a connection, bailing out:\nIOException "
                            + ex.getMessage());
        }
        */
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
