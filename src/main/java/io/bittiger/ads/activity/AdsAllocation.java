package io.bittiger.ads.activity;

import io.bittiger.ads.util.Ad;
import io.bittiger.ads.util.AllocationType;

import java.util.ArrayList;
import java.util.List;

import static io.bittiger.ads.util.Config.*;

public class AdsAllocation {
    private static AdsAllocation instance = null;

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
            return costPerClick >= MAINLINE_RESERVE_PRICE;
        } else
            return AllocationType.SIDEBAR.name().equals(level)
                    && costPerClick >= MIN_RESERVE_PRICE
                    && costPerClick < MAINLINE_RESERVE_PRICE;
    }
}
