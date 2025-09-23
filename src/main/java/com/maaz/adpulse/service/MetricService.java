package com.maaz.adpulse.service;

import com.maaz.adpulse.domain.Ad;
import com.maaz.adpulse.domain.DailyMetric;
import com.maaz.adpulse.domain.Event;
import com.maaz.adpulse.repo.AdRepository;
import com.maaz.adpulse.repo.DailyMetricRepository;
import com.maaz.adpulse.repo.EventRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

@Service
@Slf4j
public class MetricService {

    private final EventRepository eventRepo;
    private final DailyMetricRepository metricRepo;
    private final AdRepository adRepo;

    public MetricService(EventRepository eventRepo, DailyMetricRepository metricRepo, AdRepository adRepo) {
        this.eventRepo = eventRepo;
        this.metricRepo = metricRepo;
        this.adRepo = adRepo;
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
}
