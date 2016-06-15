package io.bittiger.ads.activity;

import io.bittiger.ads.model.Ad;

import java.util.List;
import java.util.ArrayList;
import java.lang.String;

public class AdsSelect {
    private static AdsSelect instance = null;

    protected AdsSelect() {
    }

    public static AdsSelect getInstance() {
        if (instance == null) {
            instance = new AdsSelect();
        }
        return instance;
    }

    public List<Ad> getMatchedAds(String[] keywords) {
        List<Ad> ads = new ArrayList<Ad>();
        if (keywords == null || keywords.length == 0) {
            return ads;
        }

        for (String keyword : keywords) {
            ads.addAll(pullMatchedAdsFromMemcached(keyword));
        }

        return ads;
    }

    public List<Ad> pullMatchedAdsFromMemcached(String keyword) {
        List<Ad> ads = new ArrayList<Ad>();
        if (keyword == null || keyword.isEmpty()) {
            return ads;
        }

        //to be discussed

        return ads;
    }
}
