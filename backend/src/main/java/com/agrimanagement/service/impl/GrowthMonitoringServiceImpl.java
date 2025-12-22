package com.agrimanagement.service.impl;

import com.agrimanagement.entity.GrowthMonitoring;
import com.agrimanagement.repository.GrowthMonitoringRepository;
import com.agrimanagement.service.GrowthMonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class GrowthMonitoringServiceImpl implements GrowthMonitoringService {
    
    @Autowired
    private GrowthMonitoringRepository growthMonitoringRepository;
    
    @Override
    public List<GrowthMonitoring> getAllGrowthMonitorings() {
        return growthMonitoringRepository.findAll();
    }
    
    @Override
    public Optional<GrowthMonitoring> getGrowthMonitoringById(Long id) {
        return growthMonitoringRepository.findById(id);
    }
    
    @Override
    public GrowthMonitoring createGrowthMonitoring(GrowthMonitoring growthMonitoring) {
        return growthMonitoringRepository.save(growthMonitoring);
    }
    
    @Override
    public GrowthMonitoring updateGrowthMonitoring(Long id, GrowthMonitoring growthMonitoringDetails) {
        GrowthMonitoring monitoring = growthMonitoringRepository.findById(id).orElse(null);
        if (monitoring != null) {
            monitoring.setMonitoringDate(growthMonitoringDetails.getMonitoringDate());
            monitoring.setHeightMeasurement(growthMonitoringDetails.getHeightMeasurement());
            monitoring.setWidthMeasurement(growthMonitoringDetails.getWidthMeasurement());
            monitoring.setHealthStatus(growthMonitoringDetails.getHealthStatus());
            monitoring.setSoilMoisture(growthMonitoringDetails.getSoilMoisture());
            monitoring.setTemperature(growthMonitoringDetails.getTemperature());
            monitoring.setHumidity(growthMonitoringDetails.getHumidity());
            monitoring.setLightIntensity(growthMonitoringDetails.getLightIntensity());
            monitoring.setPhLevel(growthMonitoringDetails.getPhLevel());
            monitoring.setNotes(growthMonitoringDetails.getNotes());
            monitoring.setPhotoUrl(growthMonitoringDetails.getPhotoUrl());
            
            return growthMonitoringRepository.save(monitoring);
        }
        return null;
    }
    
    @Override
    public void deleteGrowthMonitoring(Long id) {
        growthMonitoringRepository.deleteById(id);
    }
}