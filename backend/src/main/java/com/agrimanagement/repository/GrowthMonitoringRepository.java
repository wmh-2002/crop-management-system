package com.agrimanagement.repository;

import com.agrimanagement.entity.GrowthMonitoring;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GrowthMonitoringRepository extends JpaRepository<GrowthMonitoring, Long> {
}