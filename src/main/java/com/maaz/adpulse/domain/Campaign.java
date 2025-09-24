package com.maaz.adpulse.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "campaigns")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "advertiser_id")
    private Long advertiserId;

    @Column(name = "budget")
    private Double budget;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "name")
    private String name;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "status")
    private String status;

    @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL)
    @JsonIgnore // Prevent serialization of this field completely
    private List<Ad> ads;
}
