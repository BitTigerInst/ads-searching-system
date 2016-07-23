package io.bittiger.ads.activity;

import io.bittiger.ads.util.Ad;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AdsPricingTest {

    @Test
    public void testAdsPricing() {
        List<Ad> ads = new ArrayList<Ad>();
        double expectedFirstCPC  = 642.8671428571429;
        double expectedSecondCPC = 600;

        Ad first = initAd(1L, 800, 2.8);
        Ad second = initAd(2L, 600, 3);

        ads.add(first);
        ads.add(second);

        List<Ad> res = AdsPricing.getInstance().processPricing(ads);

        assertNotNull(res);
        assertEquals(res.size(), ads.size());
        assertEquals(expectedFirstCPC, ads.get(0).getCostPerClick(), 0.001);
        assertEquals(expectedSecondCPC, ads.get(1).getCostPerClick(), 0.001);
    }

    private Ad initAd(long adId, double bid, double qualityScore) {
        Ad ad = new Ad();
        ad.setAdId(adId);
        ad.setBid(bid);
        ad.setQualityScore(qualityScore);
        ad.setRankScore(bid * qualityScore);
        return ad;
    }
}
