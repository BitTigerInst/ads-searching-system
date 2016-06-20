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

        ads.add(initAd(1l, 2400));
        ads.add(initAd(2l, 2358));
        ads.add(initAd(3l, 2300));
        ads.add(initAd(4l, 2200));
        ads.add(initAd(5l, 2100));
        ads.add(initAd(6l, 2000));
        ads.add(initAd(7l, 1800));
        ads.add(initAd(8l, 1600));
        ads.add(initAd(9l, 1200));
        ads.add(initAd(10l, 1000));
        ads.add(initAd(11l, 800));
        ads.add(initAd(12l, 600));
        ads.add(initAd(13l, 500));
        ads.add(initAd(14l, 400));
        ads.add(initAd(15l, 300));
        ads.add(initAd(16l, 200));

        List<Ad> res1 = AdsAllocation.getInstance().allocateAds(ads, "MAINLINE");

        assertNotNull(res1);
        assertEquals(9, res1.size());
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
