package com.maaz.adpulse.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ad_id", insertable = false, updatable = false)
    private Long adId;

    @Column(name = "cost")
    private Double cost;

    @Column(name = "external_event_id")
    private String externalEventId;

    @Column(name = "ip")
    private String ip;

    @Column(name = "occurred_at")
    private LocalDateTime occurredAt;

    @Column(name = "referrer")
    private String referrer;

    @Column(name = "revenue")
    private Double revenue;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private EventType type;

    @Column(name = "user_agent")
    private String userAgent;

    @ManyToOne
    @JoinColumn(name = "ad_id")
    private Ad ad;

    public enum EventType {
        IMPRESSION, CLICK, CONVERSION
    }
}
