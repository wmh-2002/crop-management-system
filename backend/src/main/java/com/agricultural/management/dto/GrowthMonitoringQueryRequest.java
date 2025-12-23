package com.agricultural.management.dto;

import com.agricultural.management.entity.GrowthMonitoring;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrowthMonitoringQueryRequest {
    private Integer page = 1;
    private Integer size = 10;
    private Long planId;
    private LocalDate monitoringDateFrom;
    private LocalDate monitoringDateTo;
    private GrowthMonitoring.HealthStatus healthStatus;
    private Long createdBy;
    private BigDecimal minHeight;
    private BigDecimal maxHeight;
    private BigDecimal minWidth;
    private BigDecimal maxWidth;
    private BigDecimal minTemperature;
    private BigDecimal maxTemperature;
    private BigDecimal minHumidity;
    private BigDecimal maxHumidity;
    private BigDecimal minSoilMoisture;
    private BigDecimal maxSoilMoisture;
    private BigDecimal minPhLevel;
    private BigDecimal maxPhLevel;

    // Getters and Setters
    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public LocalDate getMonitoringDateFrom() {
        return monitoringDateFrom;
    }

    public void setMonitoringDateFrom(LocalDate monitoringDateFrom) {
        this.monitoringDateFrom = monitoringDateFrom;
    }

    public LocalDate getMonitoringDateTo() {
        return monitoringDateTo;
    }

    public void setMonitoringDateTo(LocalDate monitoringDateTo) {
        this.monitoringDateTo = monitoringDateTo;
    }

    public GrowthMonitoring.HealthStatus getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(GrowthMonitoring.HealthStatus healthStatus) {
        this.healthStatus = healthStatus;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public BigDecimal getMinHeight() {
        return minHeight;
    }

    public void setMinHeight(BigDecimal minHeight) {
        this.minHeight = minHeight;
    }

    public BigDecimal getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(BigDecimal maxHeight) {
        this.maxHeight = maxHeight;
    }

    public BigDecimal getMinWidth() {
        return minWidth;
    }

    public void setMinWidth(BigDecimal minWidth) {
        this.minWidth = minWidth;
    }

    public BigDecimal getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(BigDecimal maxWidth) {
        this.maxWidth = maxWidth;
    }

    public BigDecimal getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(BigDecimal minTemperature) {
        this.minTemperature = minTemperature;
    }

    public BigDecimal getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(BigDecimal maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public BigDecimal getMinHumidity() {
        return minHumidity;
    }

    public void setMinHumidity(BigDecimal minHumidity) {
        this.minHumidity = minHumidity;
    }

    public BigDecimal getMaxHumidity() {
        return maxHumidity;
    }

    public void setMaxHumidity(BigDecimal maxHumidity) {
        this.maxHumidity = maxHumidity;
    }

    public BigDecimal getMinSoilMoisture() {
        return minSoilMoisture;
    }

    public void setMinSoilMoisture(BigDecimal minSoilMoisture) {
        this.minSoilMoisture = minSoilMoisture;
    }

    public BigDecimal getMaxSoilMoisture() {
        return maxSoilMoisture;
    }

    public void setMaxSoilMoisture(BigDecimal maxSoilMoisture) {
        this.maxSoilMoisture = maxSoilMoisture;
    }

    public BigDecimal getMinPhLevel() {
        return minPhLevel;
    }

    public void setMinPhLevel(BigDecimal minPhLevel) {
        this.minPhLevel = minPhLevel;
    }

    public BigDecimal getMaxPhLevel() {
        return maxPhLevel;
    }

    public void setMaxPhLevel(BigDecimal maxPhLevel) {
        this.maxPhLevel = maxPhLevel;
    }
}
