package io.bittiger.ads.activity;

import io.bittiger.ads.util.Ad;

import java.util.List;

public class AdsRanking {
    private static AdsRanking instance = null;
    protected AdsRanking() {

    }
    public static AdsRanking getInstance() {
        if (instance == null) {
            instance = new AdsRanking();
        }
        return instance;
    }

    public List<Ad> rankAds(List<Ad> filteredAds) {
        if (filteredAds == null || filteredAds.size() == 0) {
            return filteredAds;
        }
        for (Ad ad: filteredAds) {
            double qualityScore = 0.75 * ad.getpClick() + 0.25 * ad.getRelevantScore();
            ad.setQualityScore(qualityScore);
            double rankScore = qualityScore * ad.getBid();
            ad.setRankScore(rankScore);
        }
        return filteredAds;
    }
}