package com.maaz.adpulse.repo;

import com.maaz.adpulse.domain.DailyMetric;
import com.maaz.adpulse.domain.Campaign;
import com.maaz.adpulse.domain.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyMetricRepository extends JpaRepository<DailyMetric, Long> {
    Optional<DailyMetric> findByDateAndCampaignAndAd(LocalDate date, Campaign campaign, Ad ad);
    List<DailyMetric> findByCampaign(Campaign campaign);

    // New methods for metrics and leaderboard functionality
    List<DailyMetric> findByCampaignIdAndDateBetween(Long campaignId, LocalDate start, LocalDate end);
    List<DailyMetric> findByAdIdAndDateBetween(Long adId, LocalDate start, LocalDate end);
    List<DailyMetric> findByAdId(Long adId);
}
