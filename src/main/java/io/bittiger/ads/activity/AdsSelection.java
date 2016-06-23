package io.bittiger.ads.activity;

import io.bittiger.ads.util.Ad;
import net.spy.memcached.MemcachedClient;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
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

        List<Ad> ads = new ArrayList<Ad>(set);

        return ads;
    }

    private void calculateRelevantScore(Ad ad, String[] queries) {
        String[] keywords = ad.getKeywords();
        int hit = 0;
        Arrays.sort(queries);
        for (String keyword : keywords) {
            int index = Arrays.binarySearch(queries, keyword);
            if (index >= 0) {
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
            if (adIds.add(newAd.getAdId())) {
                calculateRelevantScore(newAd, queries);
                ads.add(newAd);
            }
        }
    }
}
