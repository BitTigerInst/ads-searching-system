package io.bittiger.ads.activity;

import io.bittiger.ads.datastore.AdsDao;
import io.bittiger.ads.datastore.AdsIndex;
import io.bittiger.ads.util.Ad;
import io.bittiger.ads.util.AllocationType;
import io.bittiger.ads.util.Campaign;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import static io.bittiger.ads.util.Config.*;

public class AdsEngine {

    public boolean init() throws IOException {
        return loadAdsfile() && loadCampaignsFile();
    }

    private boolean loadAdsfile() {
        /* System.getProperty(USER_DIR) + ADS_LOCATION cannot is not file path
        readFile path should set to your own path,
        for example:
        "/Users/sleephu2/Dropbox/GitRepository/ads-searching-system" + ADS_LOCATION
        */
<<<<<<< HEAD
        String jsonData = readFile( "/Users/sleephu2/Dropbox/GitRepository/ads-searching-system" + ADS_LOCATION);
=======
        String jsonData = readFile("/Users/jli1/BT/ads-searching-system/src/main/resources/ads.json");
>>>>>>> a93fac5ed027f7f658d3bacccf99c4caae32291a
        System.out.println(System.getProperty(USER_DIR));
        JSONArray jsonArr = new JSONArray(jsonData);

        for (int i = 0; i < jsonArr.length(); i++) {
            Ad ad = new Ad();
            ad.setAdId(jsonArr.getJSONObject(i).getLong(AD_ID));
            ad.setCampaignId(jsonArr.getJSONObject(i).getLong(CAMPAIGN_ID));
            ad.setKeywords(QueryUnderstanding.getInstance().parseQuery(jsonArr.getJSONObject(i).getString(KEYWORDS)));
            ad.setBid(jsonArr.getJSONObject(i).getDouble(BID));
            ad.setpClick(jsonArr.getJSONObject(i).getDouble(PCLICK));

            if (!setAd(ad)) {
                return false;
            }
        }
        return true;
    }

    private boolean loadCampaignsFile() {
<<<<<<< HEAD
        String jsonData = readFile( "/Users/sleephu2/Dropbox/GitRepository/ads-searching-system" + CAMPAIGNS_LOCATION);
=======
        String jsonData = readFile("/Users/jli1/BT/ads-searching-system/src/main/resources/campaign.json");
>>>>>>> a93fac5ed027f7f658d3bacccf99c4caae32291a
        JSONArray jsonArr = new JSONArray(jsonData);

        for (int i = 0; i < jsonArr.length(); i++) {

            Campaign campaign = new Campaign();
            campaign.setCampaignId(jsonArr.getJSONObject(i).getLong(CAMPAIGN_ID));
            campaign.setBudget(jsonArr.getJSONObject(i).getDouble(BUDGET));

            if (!setCampaign(campaign)) {
                return false;
            }
        }
        return true;
    }

    private boolean setAd(Ad ad) {
        return AdsDao.getInstance().setAdToMongo(ad) && AdsIndex.getInstance().setAdToCache(ad);
    }

    private boolean setCampaign(Campaign campaign) {
        return AdsDao.getInstance().setCampaign(campaign);
    }

    private String readFile(String filename) {
        String result = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();
            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void selectAds(String query) {

        String[] keywords = QueryUnderstanding.getInstance().parseQuery(query);

        List<Ad> matchedAds = AdsSelection.getInstance().getMatchedAds(keywords);

        List<Ad> filteredAds = AdsFilter.getInstance().filterAds(matchedAds);

        List<Ad> rankedAds = AdsRanking.getInstance().rankAds(filteredAds);

        List<Ad> selectedSortedAds = TopKAds.getInstance().selectTopKAds(rankedAds);

        List<Ad> pricedAds = AdsPricing.getInstance().processPricing(selectedSortedAds);

        List<Ad> dedupedAds = AdsCampaignManager.getInstance().dedupeAdsByCampaignId(pricedAds);

        List<Ad> appliedBudgetAds = AdsCampaignManager.getInstance().applyBudget(dedupedAds);

        List<Ad> mainlineAds = AdsAllocation.getInstance().allocateAds(appliedBudgetAds, AllocationType.MAINLINE.name());

        List<Ad> sidebarAds = AdsAllocation.getInstance().allocateAds(appliedBudgetAds, AllocationType.SIDEBAR.name());


        for (Ad mainlineAd : mainlineAds) {
            System.out.println(mainlineAd.getAdId());
        }

        System.out.println("===============");

        for (Ad sidebar : sidebarAds) {
            System.out.println(sidebar.getAdId());
        }
    }
}
