package com.maaz.adpulse.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdLeaderboardDTO {
    private Long adId;
    private String adName;
    private String campaignName;
    private Double metricValue;

    public AdLeaderboardDTO(Long adId, String adName, String campaignName, Double metricValue) {
        this.adId = adId;
        this.adName = adName;
        this.campaignName = campaignName;
        this.metricValue = metricValue;
    }
}
