package com.agricultural.management.dto;

import com.agricultural.management.entity.PlantingPlan;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlantingPlanUpdateRequest {
    private Long farmlandId;

    private Long cropId;

    private String planName;

    @FutureOrPresent(message = "计划开始日期不能早于今天")
    private LocalDate plannedStartDate;

    @Future(message = "计划结束日期必须晚于今天")
    private LocalDate plannedEndDate;

    private LocalDate expectedHarvestDate;

    private String sowingDensity;

    private String notes;

    private PlantingPlan.PlantingStatus status;

    // Getters and Setters
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
}
