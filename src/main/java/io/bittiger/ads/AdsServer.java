package io.bittiger.ads;

import io.bittiger.ads.activity.AdsEngine;
import io.bittiger.ads.datastore.AdsDao;
import io.bittiger.ads.datastore.AdsIndex;
import io.bittiger.ads.util.Ad;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class AdsServer {
    public static void main (String[] args) throws IOException {

        AdsEngine adsEngine = new AdsEngine();

        if (adsEngine.init()) {

            Scanner scan = new Scanner(System.in);

            String query;

            do {

                System.out.println("Please enter your query (Enter Q to exit):");
                query = scan.nextLine();

                List<Ad> ads = adsEngine.selectAds(query);

                for (Ad ad : adsEngine.getMainlineAds(ads)) {
                    System.out.println(ad.getAdId());
                }

                System.out.println("===============");

                for (Ad ad : adsEngine.getSidebarAds(ads)) {
                    System.out.println(ad.getAdId());
                }

            } while (!query.equals("Q"));

            System.out.println("See you!");
        }

        AdsIndex.getInstance().shutdown();
        AdsDao.getInstance().shutdown();
    }
}
