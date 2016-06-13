package io.bittiger.ads;

public class Ad {
    private long adId;
    private long campaignId;
    private String keyWords;
    private double bid;
    private double relevantScore;
    private double pClick;
    private double rankScore;
    private double CPC;

    public Ad() {
    }

    public long getAdId() {
        return adId;
    }

    public void setAdId(long adId) {
        this.adId = adId;
    }

    public long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(long campaignId) {
        this.campaignId = campaignId;
    }

    public String getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String keyWords) {
        this.keyWords = keyWords;
    }

    public double getBid() {
        return bid;
    }

    public void setBid(double bid) {
        this.bid = bid;
    }

    public double getRelevantScore() {
        return relevantScore;
    }

    public void setRelevantScore(double relevantScore) {
        this.relevantScore = relevantScore;
    }

    public double getpClick() {
        return pClick;
    }

    public void setpClick(double pClick) {
        this.pClick = pClick;
    }

    public double getRankScore() {
        return rankScore;
    }

    public void setRankScore(double rankScore) {
        this.rankScore = rankScore;
    }

    public double getCPC() {
        return CPC;
    }

    public void setCPC(double CPC) {
        this.CPC = CPC;
    }
}
