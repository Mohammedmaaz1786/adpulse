package com.maaz.adpulse.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "ads")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Double bidAmount;
    private String creativeUrl;
    private String status;

    @ManyToOne(optional = false)
    private Campaign campaign;
}
