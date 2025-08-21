package com.maaz.adpulse.web.dto;

import lombok.Data;

@Data
public class EventRequest {
    private String externalEventId; // unique ID (from client)
    private Long adId;              // link to Ad
    private String type;            // IMPRESSION, CLICK, CONVERSION
    private String occurredAt;      // ISO datetime string
    private Double cost;            // optional
    private Double revenue;         // optional
    private String userAgent;
    private String ip;
    private String referrer;
}
