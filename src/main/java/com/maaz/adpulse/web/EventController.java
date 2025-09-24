package com.maaz.adpulse.web;

import com.maaz.adpulse.domain.Event;
import com.maaz.adpulse.service.EventService;
import com.maaz.adpulse.web.dto.EventRequest;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public Event recordEvent(@RequestBody EventRequest req) {
        // Parse LocalDateTime and convert to Instant
        LocalDateTime localDateTime = LocalDateTime.parse(req.getOccurredAt());
        Instant instant = localDateTime.atZone(ZoneOffset.UTC).toInstant();

        return eventService.recordEvent(
                req.getExternalEventId(),
                req.getAdId(),
                req.getType(),
                instant,
                req.getCost(),
                req.getRevenue(),
                req.getUserAgent(),
                req.getIp(),
                req.getReferrer()
        );
    }

    @GetMapping
    public List<Event> getAllEvents() {
        return eventService.getAllEvents();
    }

    @GetMapping("/{id}")
    public Event getEventById(@PathVariable Long id) {
        return eventService.getEventById(id);
    }

    @GetMapping("/ad/{adId}")
    public List<Event> getEventsByAdId(@PathVariable Long adId) {
        return eventService.getEventsByAdId(adId);
    }
}
