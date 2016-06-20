package io.bittiger.ads.activity;

import io.bittiger.ads.util.Ad;

import java.util.ArrayList;
import java.util.List;

import static io.bittiger.ads.util.Config.*;

public class AdsFilter {
    private static AdsFilter instance = null;

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
            if (ad.getRelevantScore() >= MIN_RELEVANT_SCORE
                    && ad.getpClick() >= MIN_PCLICK
                    && ad.getBid()    >= MIN_RESERVE_PRICE) {
                filteredAds.add(ad);
            }
        }
        return filteredAds;
    }
}
