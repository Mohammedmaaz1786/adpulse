package com.maaz.adpulse.dto;

import java.util.List;

public class AdvertiserMetricsDTO {
    private Long advertiserId;
    private List<CampaignMetricsDTO> campaignMetrics;
    private long totalImpressions;
    private long totalClicks;
    private long totalConversions;
    private double totalSpend;
    private double totalRevenue;

    // getters and setters

    public Long getAdvertiserId() {
        return advertiserId;
    }

    public void setAdvertiserId(Long advertiserId) {
        this.advertiserId = advertiserId;
    }

    public List<CampaignMetricsDTO> getCampaignMetrics() {
        return campaignMetrics;
    }

    public void setCampaignMetrics(List<CampaignMetricsDTO> campaignMetrics) {
        this.campaignMetrics = campaignMetrics;
    }

    public long getTotalImpressions() {
        return totalImpressions;
    }

    public void setTotalImpressions(long totalImpressions) {
        this.totalImpressions = totalImpressions;
    }

    public long getTotalClicks() {
        return totalClicks;
    }

    public void setTotalClicks(long totalClicks) {
        this.totalClicks = totalClicks;
    }

    public long getTotalConversions() {
        return totalConversions;
    }

    public void setTotalConversions(long totalConversions) {
        this.totalConversions = totalConversions;
    }

    public double getTotalSpend() {
        return totalSpend;
    }

    public void setTotalSpend(double totalSpend) {
        this.totalSpend = totalSpend;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}
