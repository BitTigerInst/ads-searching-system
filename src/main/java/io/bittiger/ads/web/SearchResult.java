package io.bittiger.ads.web;

import com.google.gson.Gson;
import io.bittiger.ads.activity.AdsEngine;
import io.bittiger.ads.activity.*;
import io.bittiger.ads.datastore.AdsDao;
import io.bittiger.ads.datastore.AdsIndex;
import io.bittiger.ads.util.Ad;
import io.bittiger.ads.util.AllocationType;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import static io.bittiger.ads.util.Config.*;

@WebServlet(urlPatterns = "/search")
public class SearchResult extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /*load data into MongoDB*/
    public void init() throws ServletException {
        try {
          new AdsEngine().init();
        } catch (IOException e) {
            e.printStackTrace();
        }

}
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        System.out.println("inside do POST");
        StringBuilder sb = new StringBuilder();
        BufferedReader br = request.getReader();
        String str = null;
        while ((str = br.readLine()) != null) {
            sb.append(str);
        }
        JSONObject jObj = new JSONObject(sb.toString());
        String query = jObj.getString("query");


        String[] keywords = QueryUnderstanding.getInstance().parseQuery(query);
        for (String k: keywords) {
            System.out.println("keywords:"+ k);
        }
        List<Ad> matchedAds = AdsSelection.getInstance().getMatchedAds(keywords);
        System.out.println("matched Ads!~~~~~~~~~"+matchedAds);
        List<Ad> filteredAds = null;
        if (matchedAds != null && matchedAds.size() != 0) {
            filteredAds = AdsFilter.getInstance().filterAds(matchedAds);
        }
        System.out.println("filtered Ads!~~~~~~~~~"+filteredAds);
        List<Ad> rankedAds = null;
        if (filteredAds != null && filteredAds.size() != 0) {
            rankedAds = AdsRanking.getInstance().rankAds(filteredAds);
        }
        System.out.println("ranked Ads!~~~~~~~~~"+rankedAds);
        List<Ad> selectedSortedAds = null;
        if (rankedAds != null && rankedAds.size() != 0) {
            selectedSortedAds = TopKAds.getInstance().selectTopKAds(rankedAds, 5);
        }
        System.out.println("selected Ads!~~~~~~~~~"+selectedSortedAds);
        List<Ad> pricedAds = null;
        if (selectedSortedAds != null && selectedSortedAds.size() != 0) {
            pricedAds = AdsPricing.getInstance().processPricing(selectedSortedAds);
        }
        System.out.println("pricing Ads!~~~~~~~~~"+pricedAds);
        List<Ad> mainlineAdsCandidates = null;
        if (pricedAds != null) {
            mainlineAdsCandidates = AdsAllocation.getInstance().allocateAds(pricedAds, AllocationType.MAINLINE.name());
        }
        System.out.println("mainline candidates!~~~~~~~"+mainlineAdsCandidates);
        List<Ad> dedupedMainlineAds = null;
        if (mainlineAdsCandidates != null) {
            dedupedMainlineAds = AdsCampaignManager.getInstance().dedupeAdsByCampaignId(mainlineAdsCandidates);
        }
        System.out.println("mainline deduped Ads~~~~~~~~"+dedupedMainlineAds);
        List<Ad> mainlineAds = null;
        if (dedupedMainlineAds != null) {
         mainlineAds = AdsCampaignManager.getInstance().applyBudget(dedupedMainlineAds);
        }
        System.out.println("mainlineAds~~~~~~~~~~~~~"+mainlineAds);
        System.out.println("***********************************************");
        List<Ad> sidebarAdsCandidates = null;
        if (pricedAds != null) {
            sidebarAdsCandidates = AdsAllocation.getInstance().allocateAds(pricedAds, AllocationType.SIDEBAR.name());
        }
        System.out.println("sidebar candidates~~~~~~"+sidebarAdsCandidates);
        List<Ad> dedupedSidebarAds = null;
        if (sidebarAdsCandidates != null) {
            dedupedSidebarAds = AdsCampaignManager.getInstance().dedupeAdsByCampaignId(sidebarAdsCandidates);
        }
        System.out.println("sidebar deduped Ads~~~~~~~~~"+dedupedSidebarAds);
        List<Ad> sidebarAds = null;
        if (dedupedSidebarAds != null) {
            sidebarAds = AdsCampaignManager.getInstance().applyBudget(dedupedSidebarAds);
        }
        System.out.println("sidebarAds~~~~~~~~"+sidebarAds);
        if (mainlineAds == null && sidebarAds == null) {
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("Result for " + query);
            response.getWriter().write(": No result found!");
        } else {
            response.setContentType("text/html");
   //         response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            for (int i = 0; i < 5; i++) {
                out.write("\n");
                out.println(" ");
            }
            out.println("Result for " + query + ":");
            for (int i = 0; i < 22; i++) {
                out.write("******");
            }
             out.println("****");
//            System.getProperty("line.separator");
            if (mainlineAds != null && mainlineAds.size() != 0) {
                out.println("MAINLINE: ");
                for (int i = 0; i < 22; i++) {
                    out.write("******");
                }
                out.println("****");
                int cnt = 1;
                JSONArray ma = new JSONArray();
                for (Ad mainlineAd : mainlineAds) {
                    out.println("Ad"+ cnt++ + ":\n");
                    System.out.println(mainlineAd.getAdId());
                    String mainline = new Gson().toJson(mainlineAd);
                    System.out.println("GSON to String: "+mainline);
                    JSONObject main = new JSONObject(mainline.toString());
                    String adId = String.valueOf(main.getLong(AD_ID));
                    out.write("Ad Id: "+adId + "\n");
                    String campaignId = String.valueOf(main.getLong(CAMPAIGN_ID));
                    out.write("Campaign Id: "+ campaignId+"\n");
                    List<String> key = new ArrayList<String>();
                    for (int i = 0; i < mainlineAd.getKeywords().length; i++) {
                        key.add(mainlineAd.getKeywords()[i]);
                    }
                    out.write("Keywords: "+ key.toString()+"\n");
                    String bid = String.valueOf(main.getLong(BID));
                    out.write("Bid: "+ bid +"\n");
                    double value = main.getDouble(RELEVANTSCORE);
                    int whole = (int) value;
                    int fract = (int) ((value - whole) * 100);
                    String relevantScore = String.valueOf(whole + "." + fract);
                    out.write("Relevant Score: "+ relevantScore +"\n");
                    String pclick = String.valueOf(main.getDouble("pClick"));
                    out.write("pclick: "+ pclick +"\n");
                    double qualityValue = main.getDouble(QUALITYSCORE);
                    int qualityWhole = (int) qualityValue;
                    int qualityFract = (int) ((qualityValue - qualityWhole) * 100);
                    String qualityScore = String.valueOf(qualityWhole + "." + qualityFract);
                    out.write("Quality Score:"+ qualityScore +"\n");
                    String costPerClick = String.valueOf(main.getLong("costPerClick"));
                    out.write("Cost per Click: "+ costPerClick +"\n");
                    for (int i = 0; i < 22; i++) {
                        out.write("******");
                    }
                    out.println("****");
                }
            }
            if (sidebarAds != null && sidebarAds.size() != 0) {
                int cnt = 1;
                out.write("SIDEBAR: "+"\n");
                for (int i = 0; i < 22; i++) {
                    out.write("******");
                }
                out.println("****");
                for (Ad sidebar : sidebarAds) {
                    out.write("Ad"+ cnt++ + ":\n");
                    System.out.println(sidebar.getAdId());
                    String bar = new Gson().toJson(sidebar);
                    JSONObject side = new JSONObject(bar.toString());
                    String adId = String.valueOf(side.getLong(AD_ID));
                    out.write("Ad Id: "+adId + "\n");
                    String campaignId = String.valueOf(side.getLong(CAMPAIGN_ID));
                    out.write("Campaign Id: "+ campaignId+"\n");
                    List<String> key = new ArrayList<String>();
                    for (int i = 0; i < sidebar.getKeywords().length; i++) {
                        key.add(sidebar.getKeywords()[i]);
                    }
                    out.write("Keywords: "+ key.toString()+"\n");
                    String bid = String.valueOf(side.getLong(BID));
                    out.write("Bid: "+ bid +"\n");
                    double value = side.getDouble(RELEVANTSCORE);
                    int whole = (int) value;
                    int fract = (int) ((value - whole) * 100);
                    String relevantScore = String.valueOf(whole + "." + fract);
                    out.write("Relevant Score: "+ relevantScore +"\n");
                    String pclick = String.valueOf(side.getDouble("pClick"));
                    out.write("pclick: "+ pclick +"\n");
                    double qualityValue = side.getDouble(QUALITYSCORE);
                    int qualityWhole = (int) qualityValue;
                    int qualityFract = (int) ((qualityValue - qualityWhole) * 100);
                    String qualityScore = String.valueOf(qualityWhole + "." + qualityFract);
                    out.write("Quality Score:"+ qualityScore +"\n");
                    String costPerClick = String.valueOf(side.getLong("costPerClick"));
                    out.write("Cost per Click: "+ costPerClick +"\n");
                    for (int i = 0; i < 22; i++) {
                        out.write("******");
                    }
                    out.println("****");
                }
            }
            out.flush();
            out.close();
        }


    }
    public void destroy() {
        AdsIndex.getInstance().shutdown();
        AdsDao.getInstance().shutdown();
    }
}
