package io.bittiger.ads.activity;

import io.bittiger.ads.model.Ad;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AdsSelectionTest {

    @Test
    public void testAdsSelection() {

        Ad ad1 = initAd(1, new String[]{"word1"});
        Ad ad2 = initAd(2, new String[]{"word2"});
        Ad ad3 = initAd(3, new String[]{"word1", "word2", "word3"});

        List<Ad> expectedAdsWithWord1 = new ArrayList<Ad>(Arrays.asList(ad1, ad3));
        List<Ad> expectedAdsWithWord2 = new ArrayList<Ad>(Arrays.asList(ad2, ad3));
        List<Ad> expectedAdsWithWord3 = new ArrayList<Ad>(Arrays.asList(ad3));
        List<Ad> expectedAdsWithWord12 = new ArrayList<Ad>(Arrays.asList(ad1, ad2, ad3));
        double expectedRelevantScore1 = 1.0;

        AdsDao.getInstance().setAd(ad1);
        AdsDao.getInstance().setAd(ad2);
        AdsDao.getInstance().setAd(ad3);

        List<Ad> res1 = AdsSelection.getInstance().getMatchedAds(new String[]{"word1"});
        assertEquals(expectedRelevantScore1, res1.get(0).getRelevantScore(), 0.001);
        List<Ad> res2 = AdsSelection.getInstance().getMatchedAds(new String[]{"word2"});
        List<Ad> res3 = AdsSelection.getInstance().getMatchedAds(new String[]{"word3"});
        assertEquals(expectedRelevantScore1, res3.get(0).getRelevantScore(), 0.001);
        List<Ad> res4 = AdsSelection.getInstance().getMatchedAds(new String[]{"word1", "word2"});

        assertNotNull(res1);
        assertEquals(2, res1.size());


        assertNotNull(res2);
        assertEquals(2, res2.size());
        //assertEquals(expectedAdsWithWord2.get(0), res2.get(0));
        //assertEquals(expectedAdsWithWord2.get(1), res2.get(1));

        assertNotNull(res3);
        assertEquals(1, res3.size());
        //assertEquals(expectedAdsWithWord1.get(0), res1.get(0));
        
        assertNotNull(res4);
        assertEquals(3, res4.size());
        //assertEquals(expectedAdsWithWord1.get(0), res1.get(0));
        //assertEquals(expectedAdsWithWord1.get(1), res1.get(1));
        //assertEquals(expectedAdsWithWord1.get(2), res1.get(2));*/
    }

    private Ad initAd(long adId, String[] keywords) {
        Ad ad = new Ad();
        ad.setAdId(adId);
        ad.setKeywords(keywords);
        return ad;
    }
}
