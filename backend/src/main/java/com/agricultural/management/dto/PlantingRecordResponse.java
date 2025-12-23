package com.agricultural.management.dto;

import com.agricultural.management.entity.PlantingRecord;
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
public class PlantingRecordResponse {
    private Long id;
    private Long planId;
    private PlantingRecord.OperationType operationType;
    private LocalDate operationDate;
    private BigDecimal quantityUsed;
    private Long operatorId;
    private String weatherConditions;
    private String notes;
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

    public PlantingRecord.OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(PlantingRecord.OperationType operationType) {
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

    public PlantingRecordResponse(Long id, Long planId, PlantingRecord.OperationType operationType,
                                 LocalDate operationDate, BigDecimal quantityUsed, Long operatorId,
                                 String weatherConditions, String notes, LocalDateTime createdAt,
                                 LocalDateTime updatedAt) {
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
}
