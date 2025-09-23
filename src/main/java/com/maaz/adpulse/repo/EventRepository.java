package com.maaz.adpulse.repo;

import com.maaz.adpulse.domain.Event;
import com.maaz.adpulse.domain.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    Optional<Event> findByExternalEventId(String externalEventId);
    List<Event> findByAdAndOccurredAtBetween(Ad ad, Instant start, Instant end);
    List<Event> findByAd(Ad ad);

    @Query("SELECT e FROM Event e WHERE e.ad = :ad AND DATE(e.occurredAt) = :date")
    List<Event> findByAdAndDate(@Param("ad") Ad ad, @Param("date") LocalDate date);
}
