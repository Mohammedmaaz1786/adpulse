package com.maaz.adpulse.web;

import com.maaz.adpulse.domain.Ad;
import com.maaz.adpulse.dto.AdDTO;
import com.maaz.adpulse.service.AdService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/ads")
public class AdController {

    private final AdService adService;

    public AdController(AdService adService) {
        this.adService = adService;
    }

    @GetMapping("/filter")
    public List<AdDTO> filterAds(
            @RequestParam(required = false) Long campaignId,
            @RequestParam(defaultValue = "ACTIVE") String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        // If campaignId is provided, use the simple filtering method
        if (campaignId != null) {
            List<AdDTO> ads = adService.getFilteredAds(campaignId, status);

            // Quick debug for campaignId filtering
            System.out.println("DEBUG Filter with CampaignId: Found " + ads.size() + " ads for campaignId=" + campaignId + ", status=" + status);
            ads.forEach(ad -> System.out.println("DEBUG: Ad - ID: " + ad.getId() + ", Name: " + ad.getAdName() + ", Status: " + ad.getStatus() + ", CampaignId: " + ad.getCampaignId()));

            return ads;
        }

        // Otherwise use date range filtering
        // Set default dates if not provided
        if (startDate == null) {
            startDate = LocalDate.now().minusYears(1); // Default to 1 year ago
        }
        if (endDate == null) {
            endDate = LocalDate.now().plusYears(1); // Default to 1 year from now
        }

        List<AdDTO> ads = adService.getAdsByStatusAndCampaignDates(status, startDate, endDate);

        // Quick debug - check if data is being retrieved correctly
        System.out.println("DEBUG Date Range Filter: Found " + ads.size() + " ads for status=" + status);
        ads.forEach(ad -> System.out.println("DEBUG: Ad - ID: " + ad.getId() + ", Name: " + ad.getAdName() + ", Status: " + ad.getStatus() + ", CampaignId: " + ad.getCampaignId()));

        return ads;
    }

    // Additional flexible filter endpoint with optional campaignId
    @GetMapping("/filter/advanced")
    public List<AdDTO> filterAdsAdvanced(
            @RequestParam(required = false) Long campaignId,
            @RequestParam(required = false) String status) {

        List<AdDTO> ads = adService.getAdsByOptionalCampaignIdAndStatus(campaignId, status);

        // Quick debug
        System.out.println("DEBUG Advanced: Found " + ads.size() + " ads");
        ads.forEach(ad -> System.out.println("DEBUG Advanced: Ad - ID: " + ad.getId() + ", Name: " + ad.getAdName() + ", Status: " + ad.getStatus() + ", CampaignId: " + ad.getCampaignId()));

        return ads;
    }

    @PostMapping("/{campaignId}")
    public ResponseEntity<AdDTO> createAd(@PathVariable Long campaignId, @RequestBody Ad ad) {
        Ad savedAd = adService.createAd(campaignId, ad);
        AdDTO adDTO = adService.getAdById(savedAd.getId()).orElse(null);
        return ResponseEntity.ok(adDTO);
    }

    // Existing method for backward compatibility
    @PostMapping
    public AdDTO createAd(@RequestBody Ad ad) {
        Ad savedAd = adService.createAd(ad);
        return adService.getAdById(savedAd.getId()).orElse(null);
    }

    @GetMapping
    public ResponseEntity<List<AdDTO>> getAllAds() {
        return ResponseEntity.ok(adService.getAllAds());
    }

    @GetMapping("/{adId}")
    public ResponseEntity<AdDTO> getAdById(@PathVariable Long adId) {
        return adService.getAdById(adId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{adId}")
    public ResponseEntity<AdDTO> updateAd(@PathVariable Long adId, @RequestBody Ad updatedAd) {
        Ad savedAd = adService.updateAd(adId, updatedAd);
        AdDTO adDTO = adService.getAdById(savedAd.getId()).orElse(null);
        return ResponseEntity.ok(adDTO);
    }

    @DeleteMapping("/{adId}")
    public ResponseEntity<Void> deleteAd(@PathVariable Long adId) {
        adService.deleteAd(adId);
        return ResponseEntity.noContent().build();
    }

    // New endpoint using the recommended getFilteredAds method with constructor pattern
    @GetMapping("/filter/simple")
    public List<AdDTO> filterAdsByBasicCriteria(
            @RequestParam Long campaignId,
            @RequestParam String status) {

        List<AdDTO> ads = adService.getFilteredAds(campaignId, status);

        // Quick debug for the constructor pattern method
        System.out.println("DEBUG Simple Filter: Found " + ads.size() + " ads using constructor pattern");
        ads.forEach(ad -> System.out.println("DEBUG Simple: Ad - ID: " + ad.getId() + ", Name: " + ad.getAdName() + ", Status: " + ad.getStatus() + ", CampaignId: " + ad.getCampaignId()));

        return ads;
    }

    // Debug endpoint to check what data exists
    @GetMapping("/debug/all")
    public List<AdDTO> debugAllAds() {
        List<AdDTO> allAds = adService.getAllAds();
        System.out.println("DEBUG ALL ADS: Found " + allAds.size() + " total ads in database");
        allAds.forEach(ad -> {
            System.out.println("DEBUG ALL: Ad - ID: " + ad.getId() +
                             ", AdName: '" + ad.getAdName() + "'" +
                             ", Name: '" + ad.getName() + "'" +
                             ", Status: '" + ad.getStatus() + "'" +
                             ", CampaignId: " + ad.getCampaignId() +
                             ", BidAmount: " + ad.getBidAmount());
        });
        return allAds;
    }
}