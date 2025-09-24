package com.maaz.adpulse.repo;

import com.maaz.adpulse.domain.Ad;
import com.maaz.adpulse.domain.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface AdRepository extends JpaRepository<Ad, Long> {
    List<Ad> findByCampaign(Campaign campaign);
    List<Ad> findByStatus(String status);

    @Query("SELECT a FROM Ad a WHERE a.status = :status AND a.campaign.startDate BETWEEN :start AND :end")
    List<Ad> findFilteredAds(@Param("status") String status, @Param("start") LocalDate start, @Param("end") LocalDate end);

    // Additional methods for handling optional parameters with NULL checks
    @Query("SELECT a FROM Ad a WHERE (:campaignId IS NULL OR a.campaign.id = :campaignId) AND (:status IS NULL OR a.status = :status)")
    List<Ad> findByOptionalCampaignIdAndStatus(@Param("campaignId") Long campaignId, @Param("status") String status);

    // Method specifically for campaign filtering with date range and optional status
    @Query("SELECT a FROM Ad a WHERE (:status IS NULL OR a.status = :status) AND a.campaign.startDate BETWEEN :startDate AND :endDate")
    List<Ad> findByOptionalStatusAndCampaignDateRange(@Param("status") String status, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    // Standard method for filtering by campaignId and status (required for getFilteredAds method)
    // Fixed to use campaign.id instead of campaignId since Ad entity doesn't have direct campaignId field for querying
    @Query("SELECT a FROM Ad a WHERE a.campaign.id = :campaignId AND a.status = :status")
    List<Ad> findByCampaignIdAndStatus(@Param("campaignId") Long campaignId, @Param("status") String status);
}
