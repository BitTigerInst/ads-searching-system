package io.bittiger.ads.util;

public class Config {

    // Memcached settings
    public static final String MEMCACHED_HOST_NAME = "127.0.0.1";

    public static final int MEMCACHED_PORT = 11211;

    public static final int MEMCACHED_EXPIRATION_TIME = 3600;


    // Workflow settings
    public static final int TOP_K_ADS = 5;

    public static final double MIN_RELEVANT_SCORE = 0.3;

    public static final double MIN_PCLICK = 0.1;

    public static final double MIN_RESERVE_PRICE = 500;

    public static final double MAINLINE_RESERVE_PRICE = 1200;
}
