package io.bittiger.ads.activity;

import io.bittiger.ads.model.Ad;
import net.spy.memcached.MemcachedClient;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.lang.String;

public class AdsSelection {
    private static AdsSelection instance = null;
    
    protected AdsSelection() {
    }

    public static AdsSelection getInstance() {
        if (instance == null) {
            instance = new AdsSelection();
        }
        return instance;
    }

    public List<Ad> getMatchedAds(String[] keywords) {
        List<Ad> ads = new ArrayList<Ad>();
        if (keywords == null || keywords.length == 0) {
            return new ArrayList<Ad>();
        }

        Set<Ad> set = new HashSet<Ad>();
        for (String keyword : keywords) {
            String invKey = "inv" + keyword;
            Set<Ad> curr = (Set<Ad>) AdsDao.getInstance().getAds(invKey);
            if (curr == null) {
                curr = AdsDao.getInstance().traverseFwdIndex(keyword);
            }
            addAds(set, curr);
        }
        checkAndSetRelevantScore(ads, set);
        return ads;
    }

    private void checkAndSetRelevantScore(List<Ad> ads, Set<Ad> set) {
        for (Ad ad : set) {
            // if Relevant Score was not set manually, set it as 0.5
            if (ad.getRelevantScore() <= 0) {
                ad.setRelevantScore(0.5);
            }
            ads.add(ad);
        }
    }

    private void addAds(Set<Ad> ads, Set<Ad> newAds) {
        Set<Long> adIds = new HashSet<Long>();
        for (Ad ad : ads) {
            adIds.add(ad.getAdId());
        }

        for (Ad newAd : newAds) {
            if (!adIds.contains(newAd.getAdId())) {
                ads.add(newAd);
            }
        }
    }
}
