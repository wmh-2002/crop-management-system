package com.agricultural.management.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "planting_records")
public class PlantingRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "plan_id", nullable = false)
    private Long planId;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type", nullable = false)
    private OperationType operationType;

    @Column(name = "operation_date", nullable = false)
    private LocalDate operationDate;

    @Column(name = "quantity_used", precision = 10, scale = 2)
    private BigDecimal quantityUsed;

    @Column(name = "operator_id")
    private Long operatorId;

    @Column(name = "weather_conditions", length = 255)
    private String weatherConditions;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public PlantingRecord() {
    }

    public PlantingRecord(Long id, Long planId, OperationType operationType, LocalDate operationDate,
                         BigDecimal quantityUsed, Long operatorId, String weatherConditions,
                         String notes, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.planId = planId;
        this.operationType = operationType;
        this.operationDate = operationDate;
        this.quantityUsed = quantityUsed;
        this.operatorId = operatorId;
        this.weatherConditions = weatherConditions;
        this.notes = notes;
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

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }

    public LocalDate getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(LocalDate operationDate) {
        this.operationDate = operationDate;
    }

    public BigDecimal getQuantityUsed() {
        return quantityUsed;
    }

    public void setQuantityUsed(BigDecimal quantityUsed) {
        this.quantityUsed = quantityUsed;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public String getWeatherConditions() {
        return weatherConditions;
    }

    public void setWeatherConditions(String weatherConditions) {
        this.weatherConditions = weatherConditions;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public enum OperationType {
        SOWING,        // 播种
        FERTILIZING,   // 施肥
        WATERING,      // 灌溉
        PESTICIDING,   // 施药
        HARVESTING     // 收获
    }

    public static class Builder {
        private Long id;
        private Long planId;
        private OperationType operationType;
        private LocalDate operationDate;
        private BigDecimal quantityUsed;
        private Long operatorId;
        private String weatherConditions;
        private String notes;
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

        public Builder operationType(OperationType operationType) {
            this.operationType = operationType;
            return this;
        }

        public Builder operationDate(LocalDate operationDate) {
            this.operationDate = operationDate;
            return this;
        }

        public Builder quantityUsed(BigDecimal quantityUsed) {
            this.quantityUsed = quantityUsed;
            return this;
        }

        public Builder operatorId(Long operatorId) {
            this.operatorId = operatorId;
            return this;
        }

        public Builder weatherConditions(String weatherConditions) {
            this.weatherConditions = weatherConditions;
            return this;
        }

        public Builder notes(String notes) {
            this.notes = notes;
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

        public PlantingRecord build() {
            return new PlantingRecord(id, planId, operationType, operationDate, quantityUsed,
                                    operatorId, weatherConditions, notes, createdAt, updatedAt);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
