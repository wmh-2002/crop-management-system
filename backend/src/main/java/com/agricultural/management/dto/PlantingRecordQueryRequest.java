package com.agricultural.management.dto;

import com.agricultural.management.entity.PlantingRecord;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlantingRecordQueryRequest {
    private Integer page = 1;
    private Integer size = 10;
    private Long planId;
    private PlantingRecord.OperationType operationType;
    private Long operatorId;
    private LocalDate operationDateFrom;
    private LocalDate operationDateTo;
    private String weatherConditions;

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

    public PlantingRecord.OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(PlantingRecord.OperationType operationType) {
        this.operationType = operationType;
    }

    public Long getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    public LocalDate getOperationDateFrom() {
        return operationDateFrom;
    }

    public void setOperationDateFrom(LocalDate operationDateFrom) {
        this.operationDateFrom = operationDateFrom;
    }

    public LocalDate getOperationDateTo() {
        return operationDateTo;
    }

    public void setOperationDateTo(LocalDate operationDateTo) {
        this.operationDateTo = operationDateTo;
    }

    public String getWeatherConditions() {
        return weatherConditions;
    }

    public void setWeatherConditions(String weatherConditions) {
        this.weatherConditions = weatherConditions;
    }
}
