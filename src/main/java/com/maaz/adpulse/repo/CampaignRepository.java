package com.maaz.adpulse.repo;

import com.maaz.adpulse.domain.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface CampaignRepository extends JpaRepository<Campaign, Long> {
    List<Campaign> findByStatus(String status);
    List<Campaign> findByAdvertiserId(Long advertiserId);
    List<Campaign> findByStatusAndStartDateBetween(String status, LocalDate start, LocalDate end);
}
