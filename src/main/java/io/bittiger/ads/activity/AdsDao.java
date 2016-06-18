package io.bittiger.ads.activity;

import io.bittiger.ads.util.Ad;
import net.spy.memcached.MemcachedClient;

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
        try {
            getCache().set(key, MEMCACHED_EXPIRATION_TIME, ad);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
