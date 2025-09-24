package com.maaz.adpulse.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "daily_metrics")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DailyMetric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ad_id", insertable = false, updatable = false)
    private Long adId;

    @Column(name = "campaign_id", insertable = false, updatable = false)
    private Long campaignId;

    @Column(name = "clicks")
    private Long clicks;

    @Column(name = "conversions")
    private Long conversions;

    @Column(name = "impressions")
    private Long impressions;

    @Column(name = "metric_date")
    private LocalDate metricDate;

    @Column(name = "revenue")
    private Double revenue;

    @Column(name = "spend")
    private Double spend;

    @ManyToOne
    @JoinColumn(name = "ad_id")
    private Ad ad;

    @ManyToOne
    @JoinColumn(name = "campaign_id")
    private Campaign campaign;

    // Backward compatibility getter for date field
    public LocalDate getDate() {
        return metricDate;
    }

    public void setDate(LocalDate date) {
        this.metricDate = date;
    }
}
