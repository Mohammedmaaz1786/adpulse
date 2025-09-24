package com.maaz.adpulse.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ads")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ad_name")
    private String adName;

    @Column(name = "bid_amount")
    private Double bidAmount;

    @Column(name = "campaign_id", insertable = false, updatable = false)
    private Long campaignId;

    @Column(name = "content")
    private String content;

    @Column(name = "creative_url")
    private String creativeUrl;

    @Column(name = "name")
    private String name;

    @Column(name = "status")
    private String status;

    @ManyToOne
    @JoinColumn(name = "campaign_id")
    @JsonIgnore // Prevent serialization of this field completely
    private Campaign campaign;

    // Backward compatibility getter for adId
    public Long getAdId() {
        return id;
    }

    public void setAdId(Long adId) {
        this.id = adId;
    }
}
