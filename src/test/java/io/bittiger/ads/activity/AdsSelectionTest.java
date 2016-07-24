package io.bittiger.ads.activity;

import io.bittiger.ads.datastore.AdsDao;
import io.bittiger.ads.datastore.AdsIndex;
import io.bittiger.ads.util.Ad;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AdsSelectionTest {

    @Test
    public void testAdsSelection() {

        Ad ad1 = initAd(1L, new String[]{"word1"});
        Ad ad2 = initAd(2L, new String[]{"word2"});
        Ad ad3 = initAd(3L, new String[]{"word1", "word2", "word3"});

        double expectedRelevantScore1 = 0.333;
        double expectedRelevantScore2 = 1;
        double expectedRelevantScore3 = 0.666;

        AdsDao.getInstance().setAdToMongo(ad1);
        AdsDao.getInstance().setAdToMongo(ad2);
        AdsDao.getInstance().setAdToMongo(ad3);

        AdsIndex.getInstance().setAdToCache(ad1);
        AdsIndex.getInstance().setAdToCache(ad2);
        AdsIndex.getInstance().setAdToCache(ad3);

        List<Ad> res1 = AdsSelection.getInstance().getMatchedAds(new String[]{"word1"});
        assertNotNull(res1);
        assertEquals(2, res1.size());
        for (Ad ad : res1) {
            if (ad.getAdId() == ad3.getAdId()) {
                assertEquals(expectedRelevantScore1, ad.getRelevantScore(), 0.001);
            }
            if (ad.getAdId() == ad1.getAdId()) {
                assertEquals(expectedRelevantScore2, ad.getRelevantScore(), 0.001);
            }
        }

        List<Ad> res2 = AdsSelection.getInstance().getMatchedAds(new String[]{"word2"});
        assertNotNull(res2);
        assertEquals(2, res2.size());
        for (Ad ad : res2) {
            if (ad.getAdId() == ad2.getAdId()) {
                assertEquals(expectedRelevantScore2, ad.getRelevantScore(), 0.001);
            }
            if (ad.getAdId() == ad3.getAdId()) {
                assertEquals(expectedRelevantScore1, ad.getRelevantScore(), 0.001);
            }
        }

        List<Ad> res3 = AdsSelection.getInstance().getMatchedAds(new String[]{"word3"});
        assertNotNull(res3);
        assertEquals(1, res3.size());
        assertEquals(expectedRelevantScore1, res3.get(0).getRelevantScore(), 0.001);


        List<Ad> res4 = AdsSelection.getInstance().getMatchedAds(new String[]{"word1", "word2"});
        assertNotNull(res4);
        assertEquals(3, res4.size());
        for (Ad ad : res4) {
            if (ad.getAdId() == ad1.getAdId()) {
                assertEquals(expectedRelevantScore2, ad.getRelevantScore(), 0.001);
            }
            if (ad.getAdId() == ad2.getAdId()) {
                assertEquals(expectedRelevantScore2, ad.getRelevantScore(), 0.001);
            }
            if (ad.getAdId() == ad3.getAdId()) {
                assertEquals(expectedRelevantScore3, ad.getRelevantScore(), 0.001);
            }
        }
    }

    private Ad initAd(long adId, String[] keywords) {
        Ad ad = new Ad();
        ad.setAdId(adId);
        ad.setKeywords(keywords);
        return ad;
    }
}
