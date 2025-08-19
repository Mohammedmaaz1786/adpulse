package com.maaz.adpulse.repo;

import com.maaz.adpulse.domain.Event;
import com.maaz.adpulse.domain.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByExternalEventId(String externalEventId);
    List<Event> findByAdAndOccurredAtBetween(Ad ad, Instant start, Instant end);
}
