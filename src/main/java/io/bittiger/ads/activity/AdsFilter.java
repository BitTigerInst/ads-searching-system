package io.bittiger.ads.activity;

import io.bittiger.ads.model.Ad;

import java.util.ArrayList;
import java.util.List;

public class AdsFilter {
    private static AdsFilter instance = null;
    private static double MIN_RELEVANT_SCORE = 0.3;

    protected AdsFilter() {
    }

    public static AdsFilter getInstance() {
        if (instance == null) {
            instance = new AdsFilter();
        }
        return instance;
    }

    public List<Ad> filterAds(List<Ad> unfilteredAds) {
        List<Ad> filteredAds = new ArrayList<Ad>();
        for (Ad ad : unfilteredAds) {
            if (ad.getRelevantScore() > MIN_RELEVANT_SCORE) {
                filteredAds.add(ad);
            }
        }
        return filteredAds;
    }
}
