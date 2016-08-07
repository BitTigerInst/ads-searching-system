package io.bittiger.ads.activity;

import io.bittiger.ads.datastore.AdsDao;
import io.bittiger.ads.util.Ad;
import io.bittiger.ads.util.Campaign;
import org.junit.Test;
import java.lang.String;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class AdsDaoTest {

    @Test
    public void testAds() {

        long expectedAdId = 1L;
        long expectedCampaignId = 2L;
        double expectedBid = 3;
        String[] keywords = {"word1"};

        Ad ad = new Ad();
        ad.setAdId(expectedAdId);
        ad.setCampaignId(expectedCampaignId);
        ad.setBid(expectedBid);
        ad.setKeywords(keywords);

        AdsDao.getInstance().setAdToMongo(ad);

        Ad res = AdsDao.getInstance().getAdFromMongo(expectedAdId);
        Set<Ad> adSet = AdsDao.getInstance().traverseAds("word1");
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

    @Test
    public void testCampaign() {
        long expectedCampaignId = 66L;
        double expectedBudget = 599126.5425;//10000;

        Campaign campaign = new Campaign();
        campaign.setCampaignId(expectedCampaignId);
        campaign.setBudget(expectedBudget);
        AdsDao.getInstance().setCampaign(campaign);

        Campaign actualCampaign = AdsDao.getInstance().getCampaign(expectedCampaignId);

        assertEquals(expectedCampaignId, actualCampaign.getCampaignId());
        assertEquals(expectedBudget, actualCampaign.getBudget(), 0.001);
    }

}
