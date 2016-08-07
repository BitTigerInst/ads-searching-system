package io.bittiger.ads.activity;

import io.bittiger.ads.datastore.AdsDao;
import io.bittiger.ads.datastore.AdsIndex;
import io.bittiger.ads.util.Ad;
import io.bittiger.ads.util.Campaign;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class AdsCampaignManagerTest {

    private static List<Ad> ads;
    private static Ad ad1;
    private static Ad ad2;
    private static Ad ad3;

    @BeforeClass
    public static void init() {
        ad1 = initAd(1L, 1L, 356);
        ad2 = initAd(2L, 1L, 437);
        ad3 = initAd(3L, 2L, 839);

        ads = new ArrayList<Ad>();
        ads.add(ad1);
        ads.add(ad2);
        ads.add(ad3);
    }

    @Test
    public void testSuite() {
        List<Ad> ads = testdedupeAdsByCampaignId();
        testApplyBudget(ads);
    }

    private List<Ad> testdedupeAdsByCampaignId() {

        List<Ad> dedupedAds = AdsCampaignManager.getInstance().dedupeAdsByCampaignId(ads);

        assertNotNull(dedupedAds);
        assertEquals(2, dedupedAds.size());

        return dedupedAds;
    }

    private void testApplyBudget(List<Ad> ads) {

        Campaign campaign1 = initCampaign(1L, 5000);
        Campaign campaign2 = initCampaign(2L, 5000);

        AdsIndex.getInstance().setCampaign(campaign1);
        AdsIndex.getInstance().setCampaign(campaign2);

        List<Ad> appliedBudgetAds = AdsCampaignManager.getInstance().applyBudget(ads);

        assertNotNull(appliedBudgetAds);
        assertEquals(1, appliedBudgetAds.size());
        assertEquals((double)(5000 - 356), AdsIndex.getInstance().getCampaign(1L).getBudget(), 0.001);
    }

    private static Ad initAd(long adId, long campaignId, double costPerClick) {
        Ad ad = new Ad();
        ad.setAdId(adId);
        ad.setCampaignId(campaignId);
        ad.setCostPerClick(costPerClick);
        return ad;
    }

    private Campaign initCampaign(long campaignId, double budget) {
        Campaign campaign = new Campaign();
        campaign.setCampaignId(campaignId);
        campaign.setBudget(budget);
        return campaign;
    }
}
