package com.maaz.adpulse.web;

import com.maaz.adpulse.domain.Ad;
import com.maaz.adpulse.service.AdService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ads")
public class AdController {

    private final AdService adService;
    public AdController(AdService adService) { this.adService = adService; }

    @PostMapping
    public Ad createAd(@RequestBody Ad ad) {
        return adService.createAd(ad);
    }

    @GetMapping
    public List<Ad> getAllAds() {
        return adService.getAllAds();
    }

    @GetMapping("/{id}")
    public Ad getAdById(@PathVariable Long id) {
        return adService.getAdById(id).orElseThrow(() -> new RuntimeException("Ad not found"));
    }

    @DeleteMapping("/{id}")
    public void deleteAd(@PathVariable Long id) {
        adService.deleteAd(id);
    }
}
