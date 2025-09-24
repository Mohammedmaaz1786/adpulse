package com.maaz.adpulse.service;

import com.maaz.adpulse.domain.Campaign;
import com.maaz.adpulse.domain.Ad;
import com.maaz.adpulse.dto.CampaignDTO;
import com.maaz.adpulse.dto.AdDTO;
import com.maaz.adpulse.repo.CampaignRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CampaignService {

    private final CampaignRepository campaignRepo;

    public CampaignService(CampaignRepository campaignRepo) {
        this.campaignRepo = campaignRepo;
    }

    // Entity to DTO mapping methods
    private CampaignDTO mapToDTO(Campaign campaign) {
        List<AdDTO> adDTOs = campaign.getAds() != null ?
            campaign.getAds().stream()
                .map(this::mapAdToDTO)
                .collect(Collectors.toList()) :
            List.of();

        return CampaignDTO.builder()
                .id(campaign.getId())
                .name(campaign.getName())
                .budget(campaign.getBudget())
                .status(campaign.getStatus())
                .ads(adDTOs)
                .build();
    }

    private AdDTO mapAdToDTO(Ad ad) {
        return AdDTO.builder()
                .id(ad.getId())
                .adName(ad.getAdName())
                .name(ad.getName())
                .content(ad.getContent())
                .bidAmount(ad.getBidAmount())
                .creativeUrl(ad.getCreativeUrl())
                .status(ad.getStatus())
                .campaignId(ad.getCampaign() != null ? ad.getCampaign().getId() : null)
                .build();
    }

    // Service methods returning DTOs
    public Campaign createCampaign(Campaign campaign) {
        return campaignRepo.save(campaign);
    }

    public List<CampaignDTO> getAllCampaigns() {
        return campaignRepo.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<CampaignDTO> getCampaignById(Long id) {
        return campaignRepo.findById(id)
                .map(this::mapToDTO);
    }

    public List<CampaignDTO> getCampaignsByStatusAndDate(String status, LocalDate startDate, LocalDate endDate) {
        return campaignRepo.findByStatusAndStartDateBetween(status, startDate, endDate).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public void deleteCampaign(Long id) {
        campaignRepo.deleteById(id);
    }
}
