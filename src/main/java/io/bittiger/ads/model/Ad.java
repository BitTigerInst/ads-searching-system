package io.bittiger.ads.model;

import java.io.Serializable;

public class Ad implements Serializable {
    private long adId;
    private long campaignId;
    private String[] keywords;
    private double bid;
    private double relevantScore;
    private double pClick;
    private double rankScore;
    private double costPerClick;

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

    public String[] getKeywords() {
        return keywords;
    }

    public void setKeywords(String[] keywords) {
        this.keywords = keywords;
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

    public double getCostPerClick() {
        return costPerClick;
    }

    public void setCostPerClick(double costPerClick) {
        this.costPerClick = costPerClick;
    }
}
