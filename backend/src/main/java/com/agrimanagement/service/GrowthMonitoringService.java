package com.agrimanagement.service;

import com.agrimanagement.entity.GrowthMonitoring;
import java.util.List;
import java.util.Optional;

public interface GrowthMonitoringService {
    List<GrowthMonitoring> getAllGrowthMonitorings();
    Optional<GrowthMonitoring> getGrowthMonitoringById(Long id);
    GrowthMonitoring createGrowthMonitoring(GrowthMonitoring growthMonitoring);
    GrowthMonitoring updateGrowthMonitoring(Long id, GrowthMonitoring growthMonitoringDetails);
    void deleteGrowthMonitoring(Long id);
}