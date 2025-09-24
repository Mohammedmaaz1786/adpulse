package com.maaz.adpulse.service;

import com.maaz.adpulse.domain.Ad;
import com.maaz.adpulse.domain.Campaign;
import com.maaz.adpulse.dto.AdDTO;
import com.maaz.adpulse.repo.AdRepository;
import com.maaz.adpulse.repo.CampaignRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdService {

    private final AdRepository adRepository;
    private final CampaignRepository campaignRepository;

    public AdService(AdRepository adRepository, CampaignRepository campaignRepository) {
        this.adRepository = adRepository;
        this.campaignRepository = campaignRepository;
    }

    // Entity to DTO mapping method - Updated to use explicit mapping
    private AdDTO mapToDTO(Ad ad) {
        AdDTO dto = new AdDTO();
        dto.setId(ad.getId());
        dto.setName(ad.getName());
        dto.setAdName(ad.getAdName());
        dto.setBidAmount(ad.getBidAmount());
        dto.setContent(ad.getContent());
        dto.setCreativeUrl(ad.getCreativeUrl());
        dto.setStatus(ad.getStatus());
        dto.setCampaignId(ad.getCampaign() != null ? ad.getCampaign().getId() : null);
        return dto;
    }

    // Service methods returning DTOs
    public Ad createAd(Long campaignId, Ad ad) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new RuntimeException("Campaign not found with id: " + campaignId));
        ad.setCampaign(campaign);
        return adRepository.save(ad);
    }

    // Existing method for backward compatibility
    public Ad createAd(Ad ad) {
        return adRepository.save(ad);
    }

    public List<AdDTO> getAllAds() {
        return adRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Optional<AdDTO> getAdById(Long adId) {
        return adRepository.findById(adId)
                .map(this::mapToDTO);
    }

    public Ad updateAd(Long adId, Ad updatedAd) {
        return adRepository.findById(adId).map(ad -> {
            ad.setAdName(updatedAd.getAdName());
            ad.setContent(updatedAd.getContent());
            // Update other fields if needed
            if (updatedAd.getName() != null) {
                ad.setName(updatedAd.getName());
            }
            if (updatedAd.getBidAmount() != null) {
                ad.setBidAmount(updatedAd.getBidAmount());
            }
            if (updatedAd.getCreativeUrl() != null) {
                ad.setCreativeUrl(updatedAd.getCreativeUrl());
            }
            if (updatedAd.getStatus() != null) {
                ad.setStatus(updatedAd.getStatus());
            }
            return adRepository.save(ad);
        }).orElseThrow(() -> new RuntimeException("Ad not found with id: " + adId));
    }

    public void deleteAd(Long adId) {
        adRepository.deleteById(adId);
    }

    public List<AdDTO> getAdsByStatusAndCampaignDates(String status, LocalDate startDate, LocalDate endDate) {
        // Use the new repository method that handles NULL status properly
        return adRepository.findByOptionalStatusAndCampaignDateRange(
                "ACTIVE".equals(status) ? status : null, // Only pass status if it's ACTIVE, otherwise NULL
                startDate,
                endDate
        ).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // New method for advanced filtering with optional parameters
    public List<AdDTO> getAdsByOptionalCampaignIdAndStatus(Long campaignId, String status) {
        return adRepository.findByOptionalCampaignIdAndStatus(campaignId, status).stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Recommended filtering method with explicit DTO mapping using constructor
    public List<AdDTO> getFilteredAds(Long campaignId, String status) {
        List<Ad> ads = adRepository.findByCampaignIdAndStatus(campaignId, status);
        return ads.stream()
                  .map(ad -> new AdDTO(
                      ad.getId(),
                      ad.getAdName(),
                      ad.getBidAmount(),
                      ad.getContent(),
                      ad.getStatus(),
                      ad.getCampaign() != null ? ad.getCampaign().getId() : null))
                  .collect(Collectors.toList());
    }
}
