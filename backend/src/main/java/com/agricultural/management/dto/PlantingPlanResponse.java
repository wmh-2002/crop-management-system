package com.agricultural.management.dto;

import com.agricultural.management.entity.PlantingPlan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlantingPlanResponse {
    private Long id;
    private Long farmlandId;
    private Long cropId;
    private String planName;
    private LocalDate plannedStartDate;
    private LocalDate plannedEndDate;
    private LocalDate expectedHarvestDate;
    private String sowingDensity;
    private String notes;
    private PlantingPlan.PlantingStatus status;
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

    public Long getFarmlandId() {
        return farmlandId;
    }

    public void setFarmlandId(Long farmlandId) {
        this.farmlandId = farmlandId;
    }

    public Long getCropId() {
        return cropId;
    }

    public void setCropId(Long cropId) {
        this.cropId = cropId;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public LocalDate getPlannedStartDate() {
        return plannedStartDate;
    }

    public void setPlannedStartDate(LocalDate plannedStartDate) {
        this.plannedStartDate = plannedStartDate;
    }

    public LocalDate getPlannedEndDate() {
        return plannedEndDate;
    }

    public void setPlannedEndDate(LocalDate plannedEndDate) {
        this.plannedEndDate = plannedEndDate;
    }

    public LocalDate getExpectedHarvestDate() {
        return expectedHarvestDate;
    }

    public void setExpectedHarvestDate(LocalDate expectedHarvestDate) {
        this.expectedHarvestDate = expectedHarvestDate;
    }

    public String getSowingDensity() {
        return sowingDensity;
    }

    public void setSowingDensity(String sowingDensity) {
        this.sowingDensity = sowingDensity;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public PlantingPlan.PlantingStatus getStatus() {
        return status;
    }

    public void setStatus(PlantingPlan.PlantingStatus status) {
        this.status = status;
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

    public PlantingPlanResponse(Long id, Long farmlandId, Long cropId, String planName,
                               LocalDate plannedStartDate, LocalDate plannedEndDate,
                               LocalDate expectedHarvestDate, String sowingDensity,
                               String notes, PlantingPlan.PlantingStatus status,
                               Long createdBy, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.farmlandId = farmlandId;
        this.cropId = cropId;
        this.planName = planName;
        this.plannedStartDate = plannedStartDate;
        this.plannedEndDate = plannedEndDate;
        this.expectedHarvestDate = expectedHarvestDate;
        this.sowingDensity = sowingDensity;
        this.notes = notes;
        this.status = status;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
