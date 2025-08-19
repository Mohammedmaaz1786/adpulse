package com.maaz.adpulse.service;

import com.maaz.adpulse.domain.Ad;
import com.maaz.adpulse.repo.AdRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdService {

    private final AdRepository adRepo;

    public AdService(AdRepository adRepo) {
        this.adRepo = adRepo;
    }

    public Ad createAd(Ad ad) {
        return adRepo.save(ad);
    }

    public List<Ad> getAllAds() {
        return adRepo.findAll();
    }

    public Optional<Ad> getAdById(Long id) {
        return adRepo.findById(id);
    }

    public void deleteAd(Long id) {
        adRepo.deleteById(id);
    }
}
