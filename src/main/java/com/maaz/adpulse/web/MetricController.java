package com.maaz.adpulse.web;

import com.maaz.adpulse.domain.DailyMetric;
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

    @GetMapping("/campaign/{campaignId}")
    public List<DailyMetric> getMetricsByCampaign(@PathVariable Long campaignId) {
        return metricRepo.findAll().stream()
                .filter(m -> m.getAd().getCampaign().getId().equals(campaignId))
                .toList();
    }

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
}
