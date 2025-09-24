package com.maaz.adpulse.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdDTO {
    private Long id;
    private String adName;
    private String name;
    private String content;
    private Double bidAmount;
    private String creativeUrl;
    private String status;
    private Long campaignId; // just the ID, not full object

    // Convenient constructor for mapping from entity - matches recommended pattern
    public AdDTO(Long id, String adName, Double bidAmount, String content, String status, Long campaignId) {
        this.id = id;
        this.adName = adName;
        this.bidAmount = bidAmount;
        this.content = content;
        this.status = status;
        this.campaignId = campaignId;
    }
}
