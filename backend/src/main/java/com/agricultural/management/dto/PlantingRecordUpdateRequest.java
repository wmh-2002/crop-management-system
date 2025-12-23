package com.agricultural.management.dto;

import com.agricultural.management.entity.PlantingRecord;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlantingRecordUpdateRequest {
    private Long planId;

    private PlantingRecord.OperationType operationType;

    @PastOrPresent(message = "操作日期不能晚于今天")
    private LocalDate operationDate;

    @DecimalMin(value = "0.01", message = "用量必须大于0")
    @DecimalMax(value = "99999999.99", message = "用量不能超过99999999.99")
    private BigDecimal quantityUsed;

    private Long operatorId;

    private String weatherConditions;

    private String notes;

    // Getters and Setters
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
}
