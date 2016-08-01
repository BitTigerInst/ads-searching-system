package io.bittiger.ads.web;

import com.google.gson.Gson;
import io.bittiger.ads.activity.AdsEngine;
import io.bittiger.ads.datastore.AdsDao;
import io.bittiger.ads.datastore.AdsIndex;
import io.bittiger.ads.util.Ad;
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
import java.util.Collections;
import java.util.List;

import static io.bittiger.ads.util.Config.*;

@WebServlet(urlPatterns = "/search")
public class SearchResult extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AdsEngine adsEngine = new AdsEngine();

    // load data into MongoDB and Memcached
    public void init() throws ServletException {
        try {
            adsEngine.init();
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
        String str;
        while ((str = br.readLine()) != null) {
            sb.append(str);
        }
        JSONObject jObj = new JSONObject(sb.toString());
        String query = jObj.getString("query");

        List<Ad> ads = adsEngine.selectAds(query);
        List<Ad> mainlineAds = adsEngine.getMainlineAds(ads);
        List<Ad> sidebarAds = adsEngine.getSidebarAds(ads);

        if (isNullOrEmpty(mainlineAds) && isNullOrEmpty(sidebarAds)) {
            response.setContentType("text/plain");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("Result for " + query);
            response.getWriter().write(": No result found!");
        } else {
            response.setContentType("text/html");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();

            out.println("Result for " + query + ":");
            printLine(out);

            if (!isNullOrEmpty(mainlineAds)) {
                out.println("MAINLINE: ");
                printLine(out);
                assembleOutput(mainlineAds, out);
            }

            if (!isNullOrEmpty(sidebarAds)) {
                out.write("SIDEBAR: " + "\n");
                printLine(out);
                assembleOutput(sidebarAds, out);
            }

            out.flush();
            out.close();
        }
    }

    public void destroy() {
        AdsIndex.getInstance().shutdown();
        AdsDao.getInstance().shutdown();
    }

    private boolean isNullOrEmpty(List<Ad> ads) {
        return ads == null || ads.isEmpty();
    }

    private void printLine(PrintWriter out) {
        for (int i = 0; i < 22; i++) {
            out.write("******");
        }
        out.println("****");
    }

    private String convertToDecmal(double value) {
        int whole = (int) value;
        int fract = (int) ((value - whole) * 100);
        return String.valueOf(whole + "." + fract);
    }

    private void assembleOutput(List<Ad> ads, PrintWriter out) {
        int cnt = 1;
        for (Ad ad : ads) {
            out.println("Ad" + cnt++ + ":\n");
            System.out.println(ad.getAdId());
            JSONObject jsonAd = new JSONObject(new Gson().toJson(ad));

            String adId = String.valueOf(jsonAd.getLong(AD_ID));
            out.write("Ad Id: " + adId + "\n");

            String campaignId = String.valueOf(jsonAd.getLong(CAMPAIGN_ID));
            out.write("Campaign Id: " + campaignId+"\n");

            List<String> key = new ArrayList<String>();
            Collections.addAll(key, ad.getKeywords());
            out.write("Keywords: " + key.toString() + "\n");
            String bid = String.valueOf(jsonAd.getLong(BID));

            out.write("Bid: " + bid + "\n");

            String relevantScore = convertToDecmal(jsonAd.getDouble(RELEVANTSCORE));
            out.write("Relevant Score: "+ relevantScore +"\n");

            String pclick = String.valueOf(jsonAd.getDouble("pClick"));
            out.write("pclick: " + pclick + "\n");

            String qualityScore = convertToDecmal(jsonAd.getDouble(QUALITYSCORE));
            out.write("Quality Score:" + qualityScore + "\n");

            String costPerClick = String.valueOf(jsonAd.getLong("costPerClick"));
            out.write("Cost per Click: " + costPerClick + "\n");

            printLine(out);
        }
    }
}
