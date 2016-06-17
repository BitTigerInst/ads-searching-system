package io.bittiger.ads.activity;

import io.bittiger.ads.model.Ad;
import net.spy.memcached.MemcachedClient;

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
            getCache().set(key, 3600, ad);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}