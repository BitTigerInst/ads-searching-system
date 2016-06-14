package io.bittiger.ads;

import io.bittiger.ads.activity.AdsDao;

import java.io.IOException;

public class AdsEngine {

    public static void main (String[] args) throws IOException {
        System.out.println("Hello World!");

        AdsDao.getInstance().testMemcached();
    }
}
