package com.maaz.adpulse.service;

import com.maaz.adpulse.domain.Ad;
import com.maaz.adpulse.domain.Campaign;
import com.maaz.adpulse.domain.DailyMetric;
import com.maaz.adpulse.dto.AdvertiserMetricsDTO;
import com.maaz.adpulse.domain.Event;
import com.maaz.adpulse.dto.AdLeaderboardDTO;
import com.maaz.adpulse.dto.CampaignMetricsDTO;
import com.maaz.adpulse.repo.AdRepository;
import com.maaz.adpulse.repo.CampaignRepository;
import com.maaz.adpulse.repo.DailyMetricRepository;
import com.maaz.adpulse.repo.EventRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MetricService {

    private final EventRepository eventRepo;
    private final DailyMetricRepository metricRepo;
    private final AdRepository adRepo;
    private final CampaignRepository campaignRepository;

    public MetricService(EventRepository eventRepo, DailyMetricRepository metricRepo, AdRepository adRepo, CampaignRepository campaignRepository) {
        this.eventRepo = eventRepo;
        this.metricRepo = metricRepo;
        this.adRepo = adRepo;
        this.campaignRepository = campaignRepository;
    }

    @Scheduled(cron = "0 * * * * *")
    public void aggregateDailyMetrics() {
        // Use yesterday as the target date for daily metrics
        LocalDate targetDate = LocalDate.now().minusDays(1);  // yesterday
        log.info("Aggregating daily metrics for {}", targetDate);

        aggregateForDate(targetDate);
    }

    public void aggregateForDate(LocalDate targetDate) {
        log.info("Starting daily metrics aggregation for date: {}", targetDate);

        List<Ad> ads = adRepo.findAll();
        log.info("Found {} ads to process", ads.size());

        for (Ad ad : ads) {
            // Check if metrics already exist for this date/ad to prevent duplicates
            if (metricRepo.findByDateAndCampaignAndAd(targetDate, ad.getCampaign(), ad).isPresent()) {
                log.info("Metrics already exist for ad {} on date {}, skipping", ad.getId(), targetDate);
                continue;
            }

            // Use direct SQL query instead of findAll() + filter
            List<Event> events = eventRepo.findByAdAndDate(ad, targetDate);
            log.info("Events found for ad {} on {}: {}", ad.getId(), targetDate, events.size());

            if (events.isEmpty()) {
                log.info("No events found for ad {} on date {}, skipping metrics creation", ad.getId(), targetDate);
                continue;
            }

            long impressions = events.stream().filter(e -> e.getType() == Event.EventType.IMPRESSION).count();
            long clicks = events.stream().filter(e -> e.getType() == Event.EventType.CLICK).count();
            long conversions = events.stream().filter(e -> e.getType() == Event.EventType.CONVERSION).count();

            double spend = events.stream().mapToDouble(e -> e.getCost() != null ? e.getCost() : 0.0).sum();
            double revenue = events.stream().mapToDouble(e -> e.getRevenue() != null ? e.getRevenue() : 0.0).sum();

            log.info("Calculated metrics for ad {}: impressions={}, clicks={}, conversions={}, spend={}, revenue={}",
                    ad.getId(), impressions, clicks, conversions, spend, revenue);

            DailyMetric metric = DailyMetric.builder()
                    .ad(ad)
                    .campaign(ad.getCampaign()) // Make sure campaign is set
                    .date(targetDate)
                    .impressions(impressions)
                    .clicks(clicks)
                    .conversions(conversions)
                    .spend(spend)
                    .revenue(revenue)
                    .build();

            try {
                metricRepo.save(metric);
                log.info("✅ Successfully saved daily metric for ad {} on date {}", ad.getId(), targetDate);
            } catch (Exception e) {
                log.error("❌ Failed to save daily metric for ad {} on date {}: {}", ad.getId(), targetDate, e.getMessage());
            }
        }

        log.info("✅ Daily metrics aggregation completed for {}", targetDate);
    }

    // New methods for campaign metrics and leaderboard
    public List<CampaignMetricsDTO> getMetricsByCampaign(Long campaignId, LocalDate startDate, LocalDate endDate) {
        List<DailyMetric> metrics = metricRepo.findByCampaignIdAndDateBetween(campaignId, startDate, endDate);

        return metrics.stream()
                .map(dm -> new CampaignMetricsDTO(
                        dm.getDate(),
                        dm.getImpressions(),
                        dm.getClicks(),
                        dm.getConversions(),
                        dm.getSpend(),
                        dm.getRevenue()
                ))
                .collect(Collectors.toList());
    }

    public List<CampaignMetricsDTO> getMetricsByAd(Long adId, LocalDate startDate, LocalDate endDate) {
        List<DailyMetric> metrics = metricRepo.findByAdIdAndDateBetween(adId, startDate, endDate);

        return metrics.stream()
                .map(dm -> new CampaignMetricsDTO(
                        dm.getDate(),
                        dm.getImpressions(),
                        dm.getClicks(),
                        dm.getConversions(),
                        dm.getSpend(),
                        dm.getRevenue()
                ))
                .collect(Collectors.toList());
    }

    public List<AdLeaderboardDTO> getTopAds(String metric, int limit) {
        List<Ad> ads = adRepo.findAll();

        return ads.stream()
                .map(ad -> {
                    List<DailyMetric> metrics = metricRepo.findByAdId(ad.getId());
                    double value = switch (metric.toLowerCase()) {
                        case "ctr" -> metrics.stream().mapToDouble(
                                m -> m.getImpressions() != 0 ? (m.getClicks() * 100.0 / m.getImpressions()) : 0.0
                        ).average().orElse(0.0);
                        case "roi" -> metrics.stream().mapToDouble(
                                m -> m.getSpend() != 0 ? m.getRevenue() / m.getSpend() : 0.0
                        ).average().orElse(0.0);
                        case "conversions" -> metrics.stream().mapToDouble(DailyMetric::getConversions).sum();
                        default -> 0.0;
                    };
                    return new AdLeaderboardDTO(ad.getId(), ad.getName(), ad.getCampaign().getName(), value);
                })
                .sorted(Comparator.comparingDouble(AdLeaderboardDTO::getMetricValue).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    public AdvertiserMetricsDTO getMetricsByAdvertiser(Long advertiserId, LocalDate startDate, LocalDate endDate) {
        List<Campaign> campaigns = campaignRepository.findByAdvertiserId(advertiserId);

        List<CampaignMetricsDTO> campaignMetrics = campaigns.stream()
                .map(campaign -> {
                    List<DailyMetric> metrics = metricRepo
                            .findByCampaignIdAndDateBetween(campaign.getId(), startDate, endDate);

                    CampaignMetricsDTO dto = new CampaignMetricsDTO();
                    dto.setCampaignId(campaign.getId());
                    dto.setCampaignName(campaign.getName());

                    long impressions = metrics.stream().mapToLong(DailyMetric::getImpressions).sum();
                    long clicks = metrics.stream().mapToLong(DailyMetric::getClicks).sum();
                    long conversions = metrics.stream().mapToLong(DailyMetric::getConversions).sum();
                    double spend = metrics.stream().mapToDouble(DailyMetric::getSpend).sum();
                    double revenue = metrics.stream().mapToDouble(DailyMetric::getRevenue).sum();

                    dto.setImpressions(impressions);
                    dto.setClicks(clicks);
                    dto.setConversions(conversions);
                    dto.setSpend(spend);
                    dto.setRevenue(revenue);

                    return dto;
                })
                .collect(Collectors.toList());

        AdvertiserMetricsDTO advertiserMetrics = new AdvertiserMetricsDTO();
        advertiserMetrics.setAdvertiserId(advertiserId);
        advertiserMetrics.setCampaignMetrics(campaignMetrics);

        advertiserMetrics.setTotalImpressions(campaignMetrics.stream().mapToLong(CampaignMetricsDTO::getImpressions).sum());
        advertiserMetrics.setTotalClicks(campaignMetrics.stream().mapToLong(CampaignMetricsDTO::getClicks).sum());
        advertiserMetrics.setTotalConversions(campaignMetrics.stream().mapToLong(CampaignMetricsDTO::getConversions).sum());
        advertiserMetrics.setTotalSpend(campaignMetrics.stream().mapToDouble(CampaignMetricsDTO::getSpend).sum());
        advertiserMetrics.setTotalRevenue(campaignMetrics.stream().mapToDouble(CampaignMetricsDTO::getRevenue).sum());

        return advertiserMetrics;
    }
}
