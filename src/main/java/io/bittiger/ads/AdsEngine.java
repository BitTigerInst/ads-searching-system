package io.bittiger.ads;

import io.bittiger.ads.activity.AdsAllocation;
import io.bittiger.ads.activity.AdsDao;
import io.bittiger.ads.activity.AdsFilter;
import io.bittiger.ads.activity.AdsPricing;
import io.bittiger.ads.activity.AdsRanking;
import io.bittiger.ads.activity.AdsSelection;
import io.bittiger.ads.activity.QueryUnderstanding;
import io.bittiger.ads.activity.TopKAds;
import io.bittiger.ads.util.Ad;
import io.bittiger.ads.util.AllocationType;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class AdsEngine {

    public static void main (String[] args) throws IOException {

        Scanner scan = new Scanner(System.in);

        System.out.println("Please enter your query (Enter Q to exit):");
        String query = scan.nextLine();

        while (!query.equals("Q")) {

            String[] keywords = QueryUnderstanding.getInstance().parseQuery(query);

            List<Ad> matchedAds = AdsSelection.getInstance().getMatchedAds(keywords);

            List<Ad> filteredAds = AdsFilter.getInstance().filterAds(matchedAds);

            List<Ad> rankedAds = AdsRanking.getInstance().rankAds(filteredAds);

            List<Ad> selectedSortedAds = TopKAds.getInstance().selectTopKAds(rankedAds);

            List<Ad> pricedAds = AdsPricing.getInstance().processPricing(selectedSortedAds);

            List<Ad> mainlineAds = AdsAllocation.getInstance().allocateAds(pricedAds, AllocationType.MAINLINE.name());

            List<Ad> sidebarAds = AdsAllocation.getInstance().allocateAds(pricedAds, AllocationType.SIDEBAR.name());


            for (Ad mainlineAd : mainlineAds) {
                System.out.println(mainlineAd.getAdId());
            }

            System.out.println("===============");

            for (Ad sidebar : sidebarAds) {
                System.out.println(sidebar.getAdId());
            }

            System.out.println("Please enter your query (Enter Q to exit):");
            query = scan.nextLine();
        }

        System.out.println("See you!");

        AdsDao.getInstance().shutdown();
    }
}
