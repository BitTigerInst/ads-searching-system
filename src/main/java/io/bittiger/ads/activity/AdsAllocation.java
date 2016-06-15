package io.bittiger.ads.activity;

import io.bittiger.ads.model.Ad;
import io.bittiger.ads.model.AllocationType;

import java.util.ArrayList;
import java.util.List;

public class AdsAllocation {
    private static AdsAllocation instance = null;
    private static double mainlineReservePrice = 1200;
    private static double minReservePrice = 500;

    protected AdsAllocation() {
    }

    public static AdsAllocation getInstance() {
        if (instance == null) {
            instance = new AdsAllocation();
        }
        return instance;
    }

    public List<Ad> allocateAds(List<Ad> filteredAds, String level) {
        if (filteredAds == null || filteredAds.size() == 0) {
            return filteredAds;
        }
        List<Ad> allocatedAds = new ArrayList<Ad>();

        for (Ad ad : filteredAds) {
            if (isAllocatable(ad.getCostPerClick(), level)) {
                allocatedAds.add(ad);
            }
        }

        return allocatedAds;
    }

    private boolean isAllocatable(double costPerClick, String level) {
        if (AllocationType.MAINLINE.name().equals(level)) {
            return costPerClick >= mainlineReservePrice;
        } else
            return AllocationType.SIDEBAR.name().equals(level)
                    && costPerClick >= minReservePrice
                    && costPerClick < mainlineReservePrice;
    }
}
