package com.agricultural.management.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "growth_monitoring")
public class GrowthMonitoring {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "plan_id", nullable = false)
    private Long planId;

    @Column(name = "monitoring_date", nullable = false)
    private LocalDate monitoringDate;

    @Column(name = "height_measurement", precision = 8, scale = 2)
    private BigDecimal heightMeasurement;

    @Column(name = "width_measurement", precision = 8, scale = 2)
    private BigDecimal widthMeasurement;

    @Enumerated(EnumType.STRING)
    @Column(name = "health_status")
    private HealthStatus healthStatus;

    @Column(name = "soil_moisture", precision = 5, scale = 2)
    private BigDecimal soilMoisture;

    @Column(name = "temperature", precision = 5, scale = 2)
    private BigDecimal temperature;

    @Column(name = "humidity", precision = 5, scale = 2)
    private BigDecimal humidity;

    @Column(name = "light_intensity")
    private Integer lightIntensity;

    @Column(name = "ph_level", precision = 3, scale = 2)
    private BigDecimal phLevel;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "photo_url", length = 500)
    private String photoUrl;

    @Column(name = "created_by")
    private Long createdBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public GrowthMonitoring() {
    }

    public GrowthMonitoring(Long id, Long planId, LocalDate monitoringDate, BigDecimal heightMeasurement,
                           BigDecimal widthMeasurement, HealthStatus healthStatus, BigDecimal soilMoisture,
                           BigDecimal temperature, BigDecimal humidity, Integer lightIntensity,
                           BigDecimal phLevel, String notes, String photoUrl, Long createdBy,
                           LocalDateTime createdAt, LocalDateTime updatedAt) {
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

    public HealthStatus getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(HealthStatus healthStatus) {
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

    public enum HealthStatus {
        EXCELLENT,    // 优秀
        GOOD,         // 良好
        FAIR,         // 一般
        POOR          // 较差
    }

    public static class Builder {
        private Long id;
        private Long planId;
        private LocalDate monitoringDate;
        private BigDecimal heightMeasurement;
        private BigDecimal widthMeasurement;
        private HealthStatus healthStatus;
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

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder planId(Long planId) {
            this.planId = planId;
            return this;
        }

        public Builder monitoringDate(LocalDate monitoringDate) {
            this.monitoringDate = monitoringDate;
            return this;
        }

        public Builder heightMeasurement(BigDecimal heightMeasurement) {
            this.heightMeasurement = heightMeasurement;
            return this;
        }

        public Builder widthMeasurement(BigDecimal widthMeasurement) {
            this.widthMeasurement = widthMeasurement;
            return this;
        }

        public Builder healthStatus(HealthStatus healthStatus) {
            this.healthStatus = healthStatus;
            return this;
        }

        public Builder soilMoisture(BigDecimal soilMoisture) {
            this.soilMoisture = soilMoisture;
            return this;
        }

        public Builder temperature(BigDecimal temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder humidity(BigDecimal humidity) {
            this.humidity = humidity;
            return this;
        }

        public Builder lightIntensity(Integer lightIntensity) {
            this.lightIntensity = lightIntensity;
            return this;
        }

        public Builder phLevel(BigDecimal phLevel) {
            this.phLevel = phLevel;
            return this;
        }

        public Builder notes(String notes) {
            this.notes = notes;
            return this;
        }

        public Builder photoUrl(String photoUrl) {
            this.photoUrl = photoUrl;
            return this;
        }

        public Builder createdBy(Long createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public GrowthMonitoring build() {
            return new GrowthMonitoring(id, planId, monitoringDate, heightMeasurement, widthMeasurement,
                                      healthStatus, soilMoisture, temperature, humidity, lightIntensity,
                                      phLevel, notes, photoUrl, createdBy, createdAt, updatedAt);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
