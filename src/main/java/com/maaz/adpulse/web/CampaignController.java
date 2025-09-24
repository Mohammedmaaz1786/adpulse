package com.maaz.adpulse.web;

import com.maaz.adpulse.domain.Campaign;
import com.maaz.adpulse.dto.CampaignDTO;
import com.maaz.adpulse.service.CampaignService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/campaigns")
public class CampaignController {

    private final CampaignService campaignService;
    public CampaignController(CampaignService campaignService) { this.campaignService = campaignService; }

    @PostMapping
    public CampaignDTO createCampaign(@RequestBody Campaign campaign) {
        Campaign savedCampaign = campaignService.createCampaign(campaign);
        return campaignService.getCampaignById(savedCampaign.getId()).orElse(null);
    }

    @GetMapping
    public List<CampaignDTO> getAllCampaigns() {
        return campaignService.getAllCampaigns();
    }

    @GetMapping("/{id}")
    public CampaignDTO getCampaignById(@PathVariable Long id) {
        return campaignService.getCampaignById(id)
                .orElseThrow(() -> new RuntimeException("Campaign not found"));
    }

    @DeleteMapping("/{id}")
    public void deleteCampaign(@PathVariable Long id) {
        campaignService.deleteCampaign(id);
    }

    @GetMapping("/filter")
    public List<CampaignDTO> filterCampaigns(
        @RequestParam(defaultValue = "ACTIVE") String status,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        // Set default dates if not provided
        if (startDate == null) {
            startDate = LocalDate.now().minusYears(1); // Default to 1 year ago
        }
        if (endDate == null) {
            endDate = LocalDate.now().plusYears(1); // Default to 1 year from now
        }

        return campaignService.getCampaignsByStatusAndDate(status, startDate, endDate);
    }
}
