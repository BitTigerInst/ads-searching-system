package io.bittiger.ads.activity;

import io.bittiger.ads.util.Ad;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AdsFilterTest {

    @Test
    public void testAdsFilter() {
        List<Ad> ads = new ArrayList<Ad>();
        
        ads.add(initAd(1L, 1.0, 0.3, 2400));
        ads.add(initAd(2L, 0.96, 0.5, 2600));
        ads.add(initAd(3L, 0.83, 0.4, 2010));
        ads.add(initAd(4L, 0.81, 0.27, 2042));
        ads.add(initAd(5L, 0.65, 0.34, 1999));
        ads.add(initAd(6L, 0.61, 0.15, 1936));
        ads.add(initAd(7L, 0.59, 0.07, 1269));
        ads.add(initAd(8L, 0.57, 0.12, 1111));
        ads.add(initAd(9L, 0.56, 0.06, 999));
        ads.add(initAd(10L, 0.52, 0.08, 829));
        ads.add(initAd(11L, 0.44, 0.32, 300));
        ads.add(initAd(12L, 0.35, 0.12, 600));
        ads.add(initAd(13L, 0.31, 0.11, 200));
        ads.add(initAd(14L, 0.2, 0.2, 200));
        ads.add(initAd(15L, 0.19, 0.18, 100));
        
        List<Ad> res = AdsFilter.getInstance().filterAds(ads);
        
        assertNotNull(res);
        assertEquals(9, res.size());
        assertEquals(12L, res.get(res.size() - 1).getAdId());
        assertTrue(res.get(res.size() - 1).getRelevantScore() > 0.3);
    }

    private Ad initAd(long adId, double relevantScore, double pClick, double bid) {
        Ad ad = new Ad();
        ad.setAdId(adId);
        ad.setpClick(pClick);
        ad.setBid(bid);
        ad.setRelevantScore(relevantScore);
        return ad;
    }
}
