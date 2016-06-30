package io.bittiger.ads.util;

public class Config {

    // Memcached settings
    public static final String MEMCACHED_HOST_NAME = "127.0.0.1";

    public static final int MEMCACHED_PORT = 11211;

    public static final int MEMCACHED_EXPIRATION_TIME = 3600;

    // Data resource settings
    public static final String USER_DIR = "user.dir";

    public static final String ADS_LOCATION = "/src/main/resources/ads.json";

    public static final String AD_ID = "adId";

    public static final String CAMPAIGN_ID = "campaignId";

    public static final String KEYWORDS = "keywords";

    public static final String BID = "bid";

    public static final String PCLICK = "pclick";

    // Workflow settings
    public static final double MIN_RELEVANT_SCORE = 0.3;

    public static final double MIN_PCLICK = 0.1;

    public static final double MIN_RESERVE_PRICE = 500;

    public static final double MAINLINE_RESERVE_PRICE = 1200;
}
