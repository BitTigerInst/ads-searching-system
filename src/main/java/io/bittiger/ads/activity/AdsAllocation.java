package io.bittiger.ads.activity;

import io.bittiger.ads.util.Ad;
import io.bittiger.ads.util.AllocationType;

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

    public void allocateAds(List<Ad> filteredAds) {
        if (filteredAds == null || filteredAds.size() == 0) {
            return;
        }

        for (Ad ad : filteredAds) {
            if (isInMainline(ad.getCostPerClick())) {
                ad.setAllocationType(AllocationType.MAINLINE);
            } else if (isInSideBar(ad.getCostPerClick())){
                ad.setAllocationType(AllocationType.SIDEBAR);
            }
        }

    }

    private boolean isInMainline(double costPerClick) {
        return costPerClick >= MAINLINE_RESERVE_PRICE;
    }

    private boolean isInSideBar(double costPerClick) {
        return costPerClick >= MIN_RESERVE_PRICE && costPerClick < MAINLINE_RESERVE_PRICE;
    }
}
