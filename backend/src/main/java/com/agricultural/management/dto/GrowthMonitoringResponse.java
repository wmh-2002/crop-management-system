package com.agricultural.management.dto;

import com.agricultural.management.entity.GrowthMonitoring;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrowthMonitoringResponse {
    private Long id;
    private Long planId;
    private LocalDate monitoringDate;
    private BigDecimal heightMeasurement;
    private BigDecimal widthMeasurement;
    private GrowthMonitoring.HealthStatus healthStatus;
    private BigDecimal soilMoisture;
    private BigDecimal temperature;
    private BigDecimal humidity;
    private Integer lightIntensity;
    private BigDecimal phLevel;
    private String notes;
    private String photoUrl;
    private Long createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public LocalDate getMonitoringDate() {
        return monitoringDate;
    }

    public void setMonitoringDate(LocalDate monitoringDate) {
        this.monitoringDate = monitoringDate;
    }

    public BigDecimal getHeightMeasurement() {
        return heightMeasurement;
    }

    public void setHeightMeasurement(BigDecimal heightMeasurement) {
        this.heightMeasurement = heightMeasurement;
    }

    public BigDecimal getWidthMeasurement() {
        return widthMeasurement;
    }

    public void setWidthMeasurement(BigDecimal widthMeasurement) {
        this.widthMeasurement = widthMeasurement;
    }

    public GrowthMonitoring.HealthStatus getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(GrowthMonitoring.HealthStatus healthStatus) {
        this.healthStatus = healthStatus;
    }

    public BigDecimal getSoilMoisture() {
        return soilMoisture;
    }

    public void setSoilMoisture(BigDecimal soilMoisture) {
        this.soilMoisture = soilMoisture;
    }

    public BigDecimal getTemperature() {
        return temperature;
    }

    public void setTemperature(BigDecimal temperature) {
        this.temperature = temperature;
    }

    public BigDecimal getHumidity() {
        return humidity;
    }

    public void setHumidity(BigDecimal humidity) {
        this.humidity = humidity;
    }

    public Integer getLightIntensity() {
        return lightIntensity;
    }

    public void setLightIntensity(Integer lightIntensity) {
        this.lightIntensity = lightIntensity;
    }

    public BigDecimal getPhLevel() {
        return phLevel;
    }

    public void setPhLevel(BigDecimal phLevel) {
        this.phLevel = phLevel;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public GrowthMonitoringResponse(Long id, Long planId, LocalDate monitoringDate, BigDecimal heightMeasurement,
                                   BigDecimal widthMeasurement, GrowthMonitoring.HealthStatus healthStatus,
                                   BigDecimal soilMoisture, BigDecimal temperature, BigDecimal humidity,
                                   Integer lightIntensity, BigDecimal phLevel, String notes, String photoUrl,
                                   Long createdBy, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.planId = planId;
        this.monitoringDate = monitoringDate;
        this.heightMeasurement = heightMeasurement;
        this.widthMeasurement = widthMeasurement;
        this.healthStatus = healthStatus;
        this.soilMoisture = soilMoisture;
        this.temperature = temperature;
        this.humidity = humidity;
        this.lightIntensity = lightIntensity;
        this.phLevel = phLevel;
        this.notes = notes;
        this.photoUrl = photoUrl;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
