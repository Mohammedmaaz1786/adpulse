package com.maaz.adpulse.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class CampaignMetricsDTO {
    private LocalDate date;
    private Long impressions;
    private Long clicks;
    private Long conversions;
    private Double spend;
    private Double revenue;
    private Double ctr;  // Click Through Rate
    private Double cpc;  // Cost Per Click
    private Double roi;  // Return on Investment

    public CampaignMetricsDTO(LocalDate date, Long impressions, Long clicks, Long conversions,
                              Double spend, Double revenue) {
        this.date = date;
        this.impressions = impressions;
        this.clicks = clicks;
        this.conversions = conversions;
        this.spend = spend;
        this.revenue = revenue;

        this.ctr = impressions != 0 ? (clicks.doubleValue() / impressions) * 100 : 0.0;
        this.cpc = clicks != 0 ? spend / clicks : 0.0;
        this.roi = spend != 0 ? revenue / spend : 0.0;
    }
}
