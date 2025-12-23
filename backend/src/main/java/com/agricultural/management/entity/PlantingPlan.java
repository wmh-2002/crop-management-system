package com.agricultural.management.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "planting_plans")
public class PlantingPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "farmland_id", nullable = false)
    private Long farmlandId;

    @Column(name = "crop_id", nullable = false)
    private Long cropId;

    @Column(name = "plan_name", nullable = false, length = 100)
    private String planName;

    @Column(name = "planned_start_date", nullable = false)
    private LocalDate plannedStartDate;

    @Column(name = "planned_end_date", nullable = false)
    private LocalDate plannedEndDate;

    @Column(name = "expected_harvest_date")
    private LocalDate expectedHarvestDate;

    @Column(name = "sowing_density", length = 255)
    private String sowingDensity;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlantingStatus status;

    @Column(name = "created_by")
    private Long createdBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public PlantingPlan() {
    }

    public PlantingPlan(Long id, Long farmlandId, Long cropId, String planName,
                       LocalDate plannedStartDate, LocalDate plannedEndDate,
                       LocalDate expectedHarvestDate, String sowingDensity,
                       String notes, PlantingStatus status, Long createdBy,
                       LocalDateTime createdAt, LocalDateTime updatedAt) {
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

    public PlantingStatus getStatus() {
        return status;
    }

    public void setStatus(PlantingStatus status) {
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

    public enum PlantingStatus {
        PLANNED,      // 已计划
        IN_PROGRESS,  // 进行中
        COMPLETED,    // 已完成
        CANCELLED     // 已取消
    }

    public static class Builder {
        private Long id;
        private Long farmlandId;
        private Long cropId;
        private String planName;
        private LocalDate plannedStartDate;
        private LocalDate plannedEndDate;
        private LocalDate expectedHarvestDate;
        private String sowingDensity;
        private String notes;
        private PlantingStatus status;
        private Long createdBy;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder farmlandId(Long farmlandId) {
            this.farmlandId = farmlandId;
            return this;
        }

        public Builder cropId(Long cropId) {
            this.cropId = cropId;
            return this;
        }

        public Builder planName(String planName) {
            this.planName = planName;
            return this;
        }

        public Builder plannedStartDate(LocalDate plannedStartDate) {
            this.plannedStartDate = plannedStartDate;
            return this;
        }

        public Builder plannedEndDate(LocalDate plannedEndDate) {
            this.plannedEndDate = plannedEndDate;
            return this;
        }

        public Builder expectedHarvestDate(LocalDate expectedHarvestDate) {
            this.expectedHarvestDate = expectedHarvestDate;
            return this;
        }

        public Builder sowingDensity(String sowingDensity) {
            this.sowingDensity = sowingDensity;
            return this;
        }

        public Builder notes(String notes) {
            this.notes = notes;
            return this;
        }

        public Builder status(PlantingStatus status) {
            this.status = status;
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

        public PlantingPlan build() {
            return new PlantingPlan(id, farmlandId, cropId, planName, plannedStartDate,
                                  plannedEndDate, expectedHarvestDate, sowingDensity,
                                  notes, status, createdBy, createdAt, updatedAt);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
