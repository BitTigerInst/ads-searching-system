package io.bittiger.ads.activity;

import io.bittiger.ads.util.Ad;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class AdsRankingTest {

    @Test
    public void testRankAds() {
        Ad ad1 = new Ad();
        ad1.setAdId(1L);
        ad1.setpClick(0.3);
        ad1.setRelevantScore(0.2);
        ad1.setBid(3);

        Ad ad2 = new Ad();
        ad2.setAdId(2L);
        ad2.setpClick(0.2);
        ad2.setRelevantScore(0.6);
        //   ad2.setBid(2);

        Ad ad3 = new Ad();
        ad3.setAdId(3L);
        ad3.setpClick(0.5);
        ad3.setRelevantScore(0.1);
        ad3.setBid(5);

        List<Ad> filteredAds = new ArrayList<Ad>();
        filteredAds.add(ad1);
        filteredAds.add(ad2);
        filteredAds.add(ad3);

        //initial
        assertEquals(0.0, ad1.getQualityScore(), 0);
        assertEquals(0.0, ad1.getRankScore(), 0);
        assertEquals(0.0, ad3.getQualityScore(), 0);
        assertEquals(0.0, ad3.getRankScore(), 0);

        //rankedAds result
        List<Ad> rankedAds = AdsRanking.getInstance().rankAds(filteredAds);
        assertNotNull(rankedAds);
        assertEquals(0.275, ad1.getQualityScore(), 0.001);
        assertEquals(0.825, ad1.getRankScore(), 0.001);
        assertEquals(0.4, ad3.getQualityScore(), 0.001);
        assertEquals(2, ad3.getRankScore(), 0.001);

        //ad2, without bid, should have quality score, but doesn't have rank score
        assertEquals(0.3, ad2.getQualityScore(), 0.001);
        assertEquals(0.0, ad2.getRankScore(), 0.001);

    }
}
