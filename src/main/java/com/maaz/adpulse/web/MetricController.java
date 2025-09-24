package com.maaz.adpulse.web;

import com.maaz.adpulse.dto.CampaignMetricsDTO;
import com.maaz.adpulse.dto.AdLeaderboardDTO;
import com.maaz.adpulse.repo.DailyMetricRepository;
import com.maaz.adpulse.service.MetricService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/metrics")
public class MetricController {

    private final DailyMetricRepository metricRepo;
    private final MetricService metricService;

    public MetricController(DailyMetricRepository metricRepo, MetricService metricService) {
        this.metricRepo = metricRepo;
        this.metricService = metricService;
    }

    // Manual aggregation endpoints
    @PostMapping("/aggregate/{date}")
    public String aggregateForDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        metricService.aggregateForDate(date);
        return "Manual aggregation triggered for date: " + date;
    }

    @PostMapping("/aggregate/today")
    public String aggregateForToday() {
        LocalDate today = LocalDate.now();
        metricService.aggregateForDate(today);
        return "Manual aggregation triggered for today: " + today;
    }

    @PostMapping("/aggregate/yesterday")
    public String aggregateForYesterday() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        metricService.aggregateForDate(yesterday);
        return "Manual aggregation triggered for yesterday: " + yesterday;
    }

    // Campaign and Ad metrics endpoints with date range
    @GetMapping("/campaign/{campaignId}")
    public List<CampaignMetricsDTO> getCampaignMetrics(
            @PathVariable Long campaignId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return metricService.getMetricsByCampaign(campaignId, startDate, endDate);
    }

    @GetMapping("/ad/{adId}")
    public List<CampaignMetricsDTO> getAdMetrics(
            @PathVariable Long adId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return metricService.getMetricsByAd(adId, startDate, endDate);
    }

    @GetMapping("/leaderboard")
    public List<AdLeaderboardDTO> getLeaderboard(
            @RequestParam String metric,
            @RequestParam(defaultValue = "5") int limit) {
        return metricService.getTopAds(metric, limit);
    }
}
