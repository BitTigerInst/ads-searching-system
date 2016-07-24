package io.bittiger.ads.util;

public class Config {

    // Memcached settings
    public static final String MEMCACHED_HOST_NAME = "127.0.0.1";

    public static final int MEMCACHED_PORT = 11211;

    public static final String HEROKU_MEMCACHED_HOST_NAME = "pub-memcache-10087.us-east-1-2.3.ec2.garantiadata.com";

    public static final int HEROKU_MEMCACHED_PORT = 10087;

    public static final int MEMCACHED_EXPIRATION_TIME = 3600;

    public static final String MEMCACHED_USERNAME = "memcached-app53837788";

    public static final String MEMCACHED_PASSWORD = "lQyVpS6J7e7jio2e";

    // MongoDB setting

    public static final String MONGODB_HOST_NAME = "localhost";

    public static final int MONGODB_PORT = 27017;

    public static final String HEROKU_MONGODB_HOST_NAME = "mongodb://admin:admin@ds023495.mlab.com";
            //:23495/heroku_tnl9xsxn";

    public static  final int HEROKU_MONGODB_PORT = 23495;

    public static final String ADS_DB = "heroku_tnl9xsxn";
            //"adsDB";

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
