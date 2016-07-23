package io.bittiger.ads.activity;

import io.bittiger.ads.util.Ad;

import java.util.List;

public class AdsPricing {
    private static AdsPricing instance = null;

    protected AdsPricing() {
    }

    public static AdsPricing getInstance() {
        if (instance == null) {
            instance = new AdsPricing();
        }
        return instance;
    }

    public List<Ad> processPricing(List<Ad> targetedAds) {

        if (targetedAds == null || targetedAds.size() == 0) {
            return targetedAds;
        }

        for (int i = 0; i < targetedAds.size() - 1; i++) {
            Ad curAd = targetedAds.get(i);
            Ad nextAd = targetedAds.get(i + 1);

            double cPc = nextAd.getRankScore() / curAd.getQualityScore() + 0.01;
            curAd.setCostPerClick(cPc);
        }

        Ad lastAd = targetedAds.get(targetedAds.size() - 1);
        lastAd.setCostPerClick(lastAd.getBid());
        return targetedAds;
    }
}
