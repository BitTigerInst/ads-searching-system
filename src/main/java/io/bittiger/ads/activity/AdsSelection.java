package io.bittiger.ads.activity;

import io.bittiger.ads.model.Ad;
import net.spy.memcached.MemcachedClient;

import java.io.IOException;
import java.util.*;
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
            addAds(set, curr, keywords);
        }

        for (Ad ad : set) {
            ads.add(ad);
        }

        return ads;
    }

    private void calculateRelevantScore(Ad ad, String[] queries) {
        String[] keywords = ad.getKeywords();
        int hit = 0;
        Arrays.sort(queries);
        for (String keyword : keywords) {
            if (Arrays.binarySearch(queries, keyword) >= 0) {
                ++hit;
            }
        }
        ad.setRelevantScore((double) hit / queries.length);
    }

    private void addAds(Set<Ad> ads, Set<Ad> newAds, String[] queries) {
        Set<Long> adIds = new HashSet<Long>();
        for (Ad ad : ads) {
            adIds.add(ad.getAdId());
        }

        for (Ad newAd : newAds) {
            if (!adIds.contains(newAd.getAdId())) {
                calculateRelevantScore(newAd, queries);
                ads.add(newAd);
            }
        }
    }
}
