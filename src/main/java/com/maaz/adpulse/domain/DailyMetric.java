package com.maaz.adpulse.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "daily_metrics",
        uniqueConstraints = @UniqueConstraint(columnNames = {"day", "campaign_id", "ad_id"}))
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DailyMetric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate day;

    @ManyToOne(optional = false)
    private Campaign campaign;

    @ManyToOne(optional = false)
    private Ad ad;

    private Long impressions = 0L;
    private Long clicks = 0L;
    private Long conversions = 0L;

    private Double spend = 0.0;
    private Double revenue = 0.0;
}
