package com.maaz.adpulse.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampaignDTO {
    private Long id;
    private String name;
    private Double budget;
    private String status;
    private List<AdDTO> ads;
}
