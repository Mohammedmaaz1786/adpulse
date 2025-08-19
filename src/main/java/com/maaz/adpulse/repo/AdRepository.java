package com.maaz.adpulse.repo;

import com.maaz.adpulse.domain.Ad;
import com.maaz.adpulse.domain.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AdRepository extends JpaRepository<Ad, Long> {
    List<Ad> findByCampaign(Campaign campaign);
    List<Ad> findByStatus(String status);
}
