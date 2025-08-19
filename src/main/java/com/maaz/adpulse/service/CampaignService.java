package com.maaz.adpulse.service;

import com.maaz.adpulse.domain.Campaign;
import com.maaz.adpulse.repo.CampaignRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CampaignService {

    private final CampaignRepository campaignRepo;

    public CampaignService(CampaignRepository campaignRepo) {
        this.campaignRepo = campaignRepo;
    }

    public Campaign createCampaign(Campaign campaign) {
        return campaignRepo.save(campaign);
    }

    public List<Campaign> getAllCampaigns() {
        return campaignRepo.findAll();
    }

    public Optional<Campaign> getCampaignById(Long id) {
        return campaignRepo.findById(id);
    }

    public void deleteCampaign(Long id) {
        campaignRepo.deleteById(id);
    }
}
