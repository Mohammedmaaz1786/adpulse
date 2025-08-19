package com.maaz.adpulse.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "campaigns")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double budget;

    private LocalDate startDate;
    private LocalDate endDate;

    @ManyToOne(optional = false)
    private User advertiser;

    private String status; // ACTIVE, PAUSED, ENDED
}
