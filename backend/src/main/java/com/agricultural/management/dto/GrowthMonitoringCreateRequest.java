package com.agricultural.management.dto;

import com.agricultural.management.entity.GrowthMonitoring;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GrowthMonitoringCreateRequest {
    @NotNull(message = "种植计划ID不能为空")
    private Long planId;

    @NotNull(message = "监测日期不能为空")
    @PastOrPresent(message = "监测日期不能晚于今天")
    private LocalDate monitoringDate;

    @DecimalMin(value = "0.01", message = "高度测量必须大于0")
    @DecimalMax(value = "99999.99", message = "高度测量不能超过99999.99厘米")
    private BigDecimal heightMeasurement;

    @DecimalMin(value = "0.01", message = "宽度测量必须大于0")
    @DecimalMax(value = "99999.99", message = "宽度测量不能超过99999.99厘米")
    private BigDecimal widthMeasurement;

    private GrowthMonitoring.HealthStatus healthStatus;

    @DecimalMin(value = "0.00", message = "土壤湿度不能小于0%")
    @DecimalMax(value = "100.00", message = "土壤湿度不能超过100%")
    private BigDecimal soilMoisture;

    @DecimalMin(value = "-50.00", message = "温度不能低于-50°C")
    @DecimalMax(value = "60.00", message = "温度不能高于60°C")
    private BigDecimal temperature;

    @DecimalMin(value = "0.00", message = "湿度不能小于0%")
    @DecimalMax(value = "100.00", message = "湿度不能超过100%")
    private BigDecimal humidity;

    @Min(value = 0, message = "光照强度不能小于0")
    @Max(value = 100000, message = "光照强度不能超过100000lux")
    private Integer lightIntensity;

    @DecimalMin(value = "0.00", message = "pH值不能小于0")
    @DecimalMax(value = "14.00", message = "pH值不能超过14")
    private BigDecimal phLevel;

    private String notes;

    private String photoUrl;

    // Getters and Setters
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
}
