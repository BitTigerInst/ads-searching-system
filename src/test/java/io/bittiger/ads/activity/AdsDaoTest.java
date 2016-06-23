package io.bittiger.ads.activity;

import io.bittiger.ads.util.Ad;
import org.junit.Test;
import java.lang.String;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class AdsDaoTest {

    @Test
    public void testAdsDao() {

        long expectedAdId = 1l;
        long expectedCampaignId = 2l;
        long expectedBid = 3l;
        String[] keywords = {"key1"};

        Ad ad = new Ad();
        ad.setAdId(expectedAdId);
        ad.setCampaignId(expectedCampaignId);
        ad.setBid(expectedBid);
        ad.setKeywords(keywords);

        AdsDao.getInstance().setAd(ad);

        Ad res = AdsDao.getInstance().getAd(expectedAdId);
        Set<Ad> adSet = AdsDao.getInstance().getAds("invkey1");
        String[] currKeywords = res.getKeywords();
        boolean hasAd = false;
        for (Ad tmp : adSet) {
            if (tmp.getAdId() == expectedAdId) {
                hasAd = true;
                break;
            }
        }

        assertEquals(keywords[0], currKeywords[0]);
        assertEquals(expectedBid, res.getBid(), 0.001);
        assertEquals(expectedCampaignId, res.getCampaignId(), 0.001);
        assertEquals(hasAd, true);
    }
}
