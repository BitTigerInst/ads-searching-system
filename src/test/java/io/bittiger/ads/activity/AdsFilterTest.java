package io.bittiger.ads.activity;

import io.bittiger.ads.model.Ad;
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
        
        ads.add(initAd(1l, 1.0));
        ads.add(initAd(2l, 0.96));
        ads.add(initAd(3l, 0.83));
        ads.add(initAd(4l, 0.81));
        ads.add(initAd(5l, 0.65));
        ads.add(initAd(6l, 0.61));
        ads.add(initAd(7l, 0.59));
        ads.add(initAd(8l, 0.57));
        ads.add(initAd(9l, 0.56));
        ads.add(initAd(10l, 0.52));
        ads.add(initAd(11l, 0.44));
        ads.add(initAd(12l, 0.35));
        ads.add(initAd(13l, 0.31));
        ads.add(initAd(14l, 0.2));
        ads.add(initAd(15l, 0.19));
        
        List<Ad> res = AdsFilter.getInstance().filterAds(ads);
        
        assertNotNull(res);
        assertEquals(13, res.size());
        assertEquals(13l, res.get(res.size() - 1).getAdId());
        assertTrue(res.get(res.size() - 1).getRelevantScore() > 0.3);
    }

    private Ad initAd(long adId, double relevantScore) {
        Ad ad = new Ad();
        ad.setAdId(adId);
        ad.setRelevantScore(relevantScore);
        return ad;
    }
}
