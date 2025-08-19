package com.maaz.adpulse.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "events", uniqueConstraints = @UniqueConstraint(columnNames = "externalEventId"))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String externalEventId;

    @ManyToOne(optional = false)
    private Ad ad;

    @Enumerated(EnumType.STRING)
    private EventType type;

    private Instant occurredAt;

    private Double cost;
    private Double revenue;

    private String userAgent;
    private String ip;
    private String referrer;

    public enum EventType { IMPRESSION, CLICK, CONVERSION }
}
