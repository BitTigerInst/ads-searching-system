package io.bittiger.ads.util;

import java.io.Serializable;

public class Ad implements Serializable {
    private long adId;
    private long campaignId;
    private String[] keywords;
    private double bid;
    private double relevantScore;
    private double pClick;
    private double qualityScore;
    private double rankScore;
    private double costPerClick;
    private AllocationType allocationType;

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

    public double getQualityScore() {
        return qualityScore;
    }

    public void setQualityScore(double qualityScore) {
        this.qualityScore = qualityScore;
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

    public AllocationType getAllocationType() {
        return allocationType;
    }

    public void setAllocationType(AllocationType allocationType) {
        this.allocationType = allocationType;
    }

    @Override
    public String toString() {
        return "[ Ad Id: "+getAdId()+", Campaign Id: "+ getCampaignId()+", Bid: "+ getBid()+", Relevant Score: "+getRelevantScore()+", pclick: "+ getpClick()
                +", Quality Score: "+ getQualityScore()+", CostPerClick: "+getCostPerClick()+" ]";
    }
}
