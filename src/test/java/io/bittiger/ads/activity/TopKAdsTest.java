package io.bittiger.ads.activity;

import io.bittiger.ads.model.Ad;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TopKAdsTest {

    @Test
    public void testTopKAds() {

        double rankScore1 = 0.1;
        double rankScore2 = 1.1;
        double rankScore3 = 1.2;

        Ad ad1 = new Ad();
        ad1.setRankScore(rankScore1);

        Ad ad2 = new Ad();
        ad2.setRankScore(rankScore2);

        Ad ad3 = new Ad();
        ad2.setRankScore(rankScore3);

        List<Ad> rankedAds= new ArrayList<Ad>();
        rankedAds.add(ad1);
        rankedAds.add(ad2);
        rankedAds.add(ad3);

        List<Ad> selectedAds = TopKAds.getInstance().selectTopKAds(rankedAds, 1);
        List<Ad> selectedAds2 = TopKAds.getInstance().selectTopKAds(rankedAds, 2);

        assertEquals(ad3.getRankScore(), selectedAds.get(0).getRankScore(), 1.2);
        assertEquals(ad3.getRankScore(), selectedAds2.get(0).getRankScore(), 1.2);

        assertEquals(ad2.getRankScore(), selectedAds2.get(1).getRankScore(), 1.1);


    }
}
