package io.bittiger.ads;

import io.bittiger.ads.activity.AdsEngine;
import io.bittiger.ads.datastore.AdsDao;
import io.bittiger.ads.datastore.AdsIndex;

import java.io.IOException;
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

                adsEngine.selectAds(query);

            } while (!query.equals("Q"));

            System.out.println("See you!");
        }

        AdsIndex.getInstance().shutdown();
        AdsDao.getInstance().shutdown();
    }
}
