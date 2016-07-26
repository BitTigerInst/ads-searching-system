package io.bittiger.ads.activity;

import io.bittiger.ads.datastore.AdsIndex;
import io.bittiger.ads.util.Ad;
import io.bittiger.ads.util.Campaign;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.bittiger.ads.util.Config.MIN_RESERVE_PRICE;

public class AdsCampaignManager {
    private static AdsCampaignManager instance = null;

    protected AdsCampaignManager() {
    }

    public static AdsCampaignManager getInstance() {
        if (instance == null) {
            instance = new AdsCampaignManager();
        }
        return instance;
    }

    public List<Ad> dedupeAdsByCampaignId(List<Ad> candidateAds) {
        Set<Long> hashSet = new HashSet<Long>();
        List<Ad> dedupedAds = new ArrayList<Ad>();

        for (Ad ad : candidateAds) {
            if (hashSet.add(ad.getCampaignId())) {
                dedupedAds.add(ad);
            }
        }
        return dedupedAds;
    }

    public List<Ad> applyBudget(List<Ad> candidateAds) {
        List<Ad> ads = new ArrayList<Ad>();

        for (int i = 0; i < candidateAds.size() - 1; i++) {
            Ad ad = candidateAds.get(i);
            long campaignId = ad.getCampaignId();
            Campaign campaign = AdsIndex.getInstance().getCampaign(campaignId);
            double budget = campaign.getBudget();
            System.out.println("budget~~"+budget);
            if (ad.getCostPerClick() <= budget && ad.getCostPerClick() >= MIN_RESERVE_PRICE) {
                budget -= ad.getCostPerClick();
                campaign.setBudget(budget);
                AdsIndex.getInstance().setCampaign(campaign);
                ads.add(ad);
            }
        }
        return ads;
    }
}
