package io.bittiger.ads.activity;
import io.bittiger.ads.model.Ad;
import java.util.*;

public class RankAds {
    private static RankAds instance = null;
    protected RankAds() {

    }
    public static RankAds getInstance() {
        if (instance == null) {
            instance = new RankAds();
        }
        return instance;
    }

    public List<Ad> rankAds(List<Ad> filteredAds) {
        List<Ad> rankedAds = new ArrayList<Ad>();
        if (filteredAds == null || filteredAds.size() == 0) {
            return rankedAds;
        }
        for (Ad ad: filteredAds) {
            double qualityScore = 0.75 * ad.getpClick() + 0.25 * ad.getRelevantScore();
            ad.setQualityScore(qualityScore);
            double rankScore = qualityScore * ad.getBid();
            ad.setRankScore(rankScore);
        }
        return rankedAds;
    }
}
