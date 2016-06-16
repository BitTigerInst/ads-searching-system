package io.bittiger.ads.activity;

import io.bittiger.ads.model.Ad;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
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
        
        try {
            MemcachedClient cache = AdsDao.getCache();
            Set<ad> set = new HashSet<>();
            for (String keyword : keywords) {
                Set<ad> curr = (Set<ad>) cache.getAds("inv" + keyword);
                if (curr == null) {
                    curr = traverseFwdIndex(keyword);
                }
                
                set.addAll(curr);
            }
            ads.addAll(set);
            return ads;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Set<Ad> traverseFwdIndex(String keyword) {
        Set<Ad> ads = new HashSet<>();
        try {
            MemcachedClient cache = AdsDao.getCache();
            
            /* do nothing for now
            since spymemcached does not support traversing all the keys, 
            this method will be used when we setup an independent 
            database/HashMap which supports traversing keys */

            return ads;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
