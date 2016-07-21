package io.bittiger.ads.activity;

import io.bittiger.ads.util.Ad;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AdsAllocationTest {

    @Test
    public void testAdsAllocation() {
        List<Ad> ads = new ArrayList<Ad>();

        ads.add(initAd(1L, 2400));
        ads.add(initAd(2L, 2358));
        ads.add(initAd(3L, 2300));
        ads.add(initAd(4L, 2200));
        ads.add(initAd(5L, 2100));
        ads.add(initAd(6L, 2000));
        ads.add(initAd(7L, 1800));
        ads.add(initAd(8L, 1600));
        ads.add(initAd(9L, 1200));
        ads.add(initAd(10L, 1000));
        ads.add(initAd(11L, 800));
        ads.add(initAd(12L, 600));
        ads.add(initAd(13L, 500));
        ads.add(initAd(14L, 400));
        ads.add(initAd(15L, 300));
        ads.add(initAd(16L, 200));

        List<Ad> res1 = AdsAllocation.getInstance().allocateAds(ads, "MAINLINE");

        assertNotNull(res1);
        assertEquals(11, res1.size());
        assertTrue(res1.get(5).getCostPerClick() >= 1200);

        List<Ad> res2 = AdsAllocation.getInstance().allocateAds(ads, "SIDEBAR");
        assertNotNull(res2);
        assertEquals(4, res2.size());
        assertTrue(res1.get(3).getCostPerClick() >= 500);
    }

    private Ad initAd(long adId, double costPerClick) {
        Ad ad = new Ad();
        ad.setAdId(adId);
        ad.setCostPerClick(costPerClick);
        return ad;
    }
}
