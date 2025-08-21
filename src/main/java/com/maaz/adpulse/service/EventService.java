package com.maaz.adpulse.service;

import com.maaz.adpulse.domain.Ad;
import com.maaz.adpulse.domain.Event;
import com.maaz.adpulse.repo.AdRepository;
import com.maaz.adpulse.repo.EventRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class EventService {

    private final EventRepository eventRepo;
    private final AdRepository adRepo;

    public EventService(EventRepository eventRepo, AdRepository adRepo) {
        this.eventRepo = eventRepo;
        this.adRepo = adRepo;
    }

    public Event recordEvent(String externalEventId, Long adId, String type,
                             Instant occurredAt, Double cost, Double revenue,
                             String userAgent, String ip, String referrer) {

        // check duplicate
        if (eventRepo.findByExternalEventId(externalEventId).isPresent()) {
            throw new RuntimeException("Duplicate eventId: " + externalEventId);
        }

        Ad ad = adRepo.findById(adId)
                .orElseThrow(() -> new RuntimeException("Ad not found"));

        Event event = Event.builder()
                .externalEventId(externalEventId)
                .ad(ad)
                .type(Event.EventType.valueOf(type.toUpperCase()))
                .occurredAt(occurredAt)
                .cost(cost)
                .revenue(revenue)
                .userAgent(userAgent)
                .ip(ip)
                .referrer(referrer)
                .build();

        return eventRepo.save(event);
    }
}
