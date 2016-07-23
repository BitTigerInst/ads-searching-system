package io.bittiger.ads.activity;

import io.bittiger.ads.util.Ad;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TopKAdsTest {

    @Test
    public void testTopKAds() {

        double rankScore1 = 0.1;
        double rankScore2 = 1.1;
        double rankScore3 = 1.2;

        Ad ad1 = new Ad();
        ad1.setAdId(1L);
        ad1.setRankScore(rankScore1);

        Ad ad2 = new Ad();
        ad2.setAdId(2L);
        ad2.setRankScore(rankScore2);


        Ad ad3 = new Ad();
        ad3.setAdId(3L);
        ad3.setRankScore(rankScore3);

        List<Ad> rankedAds= new ArrayList<Ad>();
        rankedAds.add(ad1);
        rankedAds.add(ad2);
        rankedAds.add(ad3);

        List<Ad> selectedAds = TopKAds.getInstance().selectTopKAds(rankedAds, 1);
        List<Ad> selectedAds2 = TopKAds.getInstance().selectTopKAds(rankedAds, 2);

        //selectedAds result
        assertNotNull(selectedAds);
        assertEquals(ad3.getRankScore(), selectedAds.get(0).getRankScore(), 0.001);
        assertEquals(ad3.getAdId(), selectedAds.get(0).getAdId());

        //selectedAds2 result
        assertNotNull(selectedAds2);
        assertEquals(ad3.getRankScore(), selectedAds2.get(0).getRankScore(), 0.001);
        assertSame(ad3, selectedAds2.get(0));
        assertNotNull(selectedAds2.get(1));
        assertSame("top 2", ad2, selectedAds2.get(1));
        assertEquals(ad2.getRankScore(), selectedAds2.get(1).getRankScore(), 0.001);


    }
}
