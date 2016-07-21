package io.bittiger.ads.util;

public class Config {

    // Memcached settings
    public static final String MEMCACHED_HOST_NAME = "127.0.0.1";

    public static final int MEMCACHED_PORT = 11211;

    public static final int MEMCACHED_EXPIRATION_TIME = 3600;

    // MongoDB setting

    public static final String MONGODB_HOST_NAME = "localhost";

    public static final int MONGODB_PORT = 27017;

    public static final String ADS_DB = "adsDB";

    public static final String ADS_COLLECTION = "adsCollection";

    public static final String CAMPAIGNS_COLLECTION = "campaignsCollection";

    // Data resource settings
    public static final String USER_DIR = "user.dir";

    public static final String ADS_LOCATION = "/src/main/resources/ads.json";

    public static final String CAMPAIGNS_LOCATION = "/src/main/resources/campaign.json";

    public static final String AD_ID = "adId";

    public static final String CAMPAIGN_ID = "campaignId";

    public static final String KEYWORDS = "keywords";

    public static final String BID = "bid";

    public static final String PCLICK = "pclick";

    public static final String RELEVANTSCORE= "relevantScore";

    public static final String QUALITYSCORE = "qualityScore";

    public static final String BUDGET = "budget";

    // Workflow settings
    public static final int TOP_K_ADS = 5;

    public static final double MIN_RELEVANT_SCORE = 0.3;

    public static final double MIN_PCLICK = 0.1;

    public static final double MIN_RESERVE_PRICE = 300;

    public static final double MAINLINE_RESERVE_PRICE = 700;


}
