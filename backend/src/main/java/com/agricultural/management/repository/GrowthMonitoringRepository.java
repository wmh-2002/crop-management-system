package com.agricultural.management.repository;

import com.agricultural.management.entity.GrowthMonitoring;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface GrowthMonitoringRepository extends JpaRepository<GrowthMonitoring, Long>, JpaSpecificationExecutor<GrowthMonitoring> {

    List<GrowthMonitoring> findByPlanId(Long planId);

    List<GrowthMonitoring> findByPlanIdOrderByMonitoringDate(Long planId);

    List<GrowthMonitoring> findByHealthStatus(GrowthMonitoring.HealthStatus healthStatus);

    List<GrowthMonitoring> findByCreatedBy(Long createdBy);

    List<GrowthMonitoring> findByMonitoringDateBetween(LocalDate startDate, LocalDate endDate);

    List<GrowthMonitoring> findByPlanIdAndMonitoringDateBetween(Long planId, LocalDate startDate, LocalDate endDate);

    List<GrowthMonitoring> findByPlanIdAndHealthStatus(Long planId, GrowthMonitoring.HealthStatus healthStatus);

    List<GrowthMonitoring> findByTemperatureBetween(BigDecimal minTemp, BigDecimal maxTemp);

    List<GrowthMonitoring> findBySoilMoistureBetween(BigDecimal minMoisture, BigDecimal maxMoisture);

    List<GrowthMonitoring> findByPhLevelBetween(BigDecimal minPh, BigDecimal maxPh);

    boolean existsByPlanIdAndMonitoringDate(Long planId, LocalDate monitoringDate);

    @Query("SELECT g.healthStatus, COUNT(g) FROM GrowthMonitoring g GROUP BY g.healthStatus")
    List<Object[]> countByHealthStatus();

    @Query("SELECT g, p, c FROM GrowthMonitoring g " +
           "JOIN PlantingPlan p ON g.planId = p.id " +
           "JOIN Crop c ON p.cropId = c.id " +
           "ORDER BY c.name, g.monitoringDate")
    List<Object[]> findAllWithCropInfo();
}
