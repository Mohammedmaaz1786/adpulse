package com.maaz.adpulse.web;

import com.maaz.adpulse.domain.Event;
import com.maaz.adpulse.service.EventService;
import com.maaz.adpulse.web.dto.EventRequest;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public Event recordEvent(@RequestBody EventRequest req) {
        return eventService.recordEvent(
                req.getExternalEventId(),
                req.getAdId(),
                req.getType(),
                Instant.parse(req.getOccurredAt()),
                req.getCost(),
                req.getRevenue(),
                req.getUserAgent(),
                req.getIp(),
                req.getReferrer()
        );
    }
}
