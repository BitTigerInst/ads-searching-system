package io.bittiger.ads.util;

import java.io.Serializable;

public class Campaign implements Serializable {
    private long campaignId;
    private double budget;

    public Campaign() {
    }

    public long getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(long campaignId) {
        this.campaignId = campaignId;
    }

    public double getBudget() {
        return budget;
    }

    public void setBudget(double budget) {
        this.budget = budget;
    }
}
