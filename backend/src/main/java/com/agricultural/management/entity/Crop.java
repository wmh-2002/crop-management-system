package com.agricultural.management.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "crops")
public class Crop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 100)
    private String variety;

    @Column(name = "planting_season", length = 50)
    private String plantingSeason;

    @Column(name = "growth_period")
    private Integer growthPeriod;

    @Column(name = "expected_yield", precision = 10, scale = 2)
    private BigDecimal expectedYield;

    @Column(name = "water_needs", length = 255)
    private String waterNeeds;

    @Column(name = "fertilizer_needs", length = 255)
    private String fertilizerNeeds;

    @Column(name = "disease_info", columnDefinition = "TEXT")
    private String diseaseInfo;

    @Column(columnDefinition = "TEXT")
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Crop() {
    }

    public Crop(Long id, String name, String variety, String plantingSeason, Integer growthPeriod,
               BigDecimal expectedYield, String waterNeeds, String fertilizerNeeds,
               String diseaseInfo, String description, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.variety = variety;
        this.plantingSeason = plantingSeason;
        this.growthPeriod = growthPeriod;
        this.expectedYield = expectedYield;
        this.waterNeeds = waterNeeds;
        this.fertilizerNeeds = fertilizerNeeds;
        this.diseaseInfo = diseaseInfo;
        this.description = description;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public String getPlantingSeason() {
        return plantingSeason;
    }

    public void setPlantingSeason(String plantingSeason) {
        this.plantingSeason = plantingSeason;
    }

    public Integer getGrowthPeriod() {
        return growthPeriod;
    }

    public void setGrowthPeriod(Integer growthPeriod) {
        this.growthPeriod = growthPeriod;
    }

    public BigDecimal getExpectedYield() {
        return expectedYield;
    }

    public void setExpectedYield(BigDecimal expectedYield) {
        this.expectedYield = expectedYield;
    }

    public String getWaterNeeds() {
        return waterNeeds;
    }

    public void setWaterNeeds(String waterNeeds) {
        this.waterNeeds = waterNeeds;
    }

    public String getFertilizerNeeds() {
        return fertilizerNeeds;
    }

    public void setFertilizerNeeds(String fertilizerNeeds) {
        this.fertilizerNeeds = fertilizerNeeds;
    }

    public String getDiseaseInfo() {
        return diseaseInfo;
    }

    public void setDiseaseInfo(String diseaseInfo) {
        this.diseaseInfo = diseaseInfo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public static class Builder {
        private Long id;
        private String name;
        private String variety;
        private String plantingSeason;
        private Integer growthPeriod;
        private BigDecimal expectedYield;
        private String waterNeeds;
        private String fertilizerNeeds;
        private String diseaseInfo;
        private String description;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder variety(String variety) {
            this.variety = variety;
            return this;
        }

        public Builder plantingSeason(String plantingSeason) {
            this.plantingSeason = plantingSeason;
            return this;
        }

        public Builder growthPeriod(Integer growthPeriod) {
            this.growthPeriod = growthPeriod;
            return this;
        }

        public Builder expectedYield(BigDecimal expectedYield) {
            this.expectedYield = expectedYield;
            return this;
        }

        public Builder waterNeeds(String waterNeeds) {
            this.waterNeeds = waterNeeds;
            return this;
        }

        public Builder fertilizerNeeds(String fertilizerNeeds) {
            this.fertilizerNeeds = fertilizerNeeds;
            return this;
        }

        public Builder diseaseInfo(String diseaseInfo) {
            this.diseaseInfo = diseaseInfo;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
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

        public Crop build() {
            return new Crop(id, name, variety, plantingSeason, growthPeriod, expectedYield,
                          waterNeeds, fertilizerNeeds, diseaseInfo, description, createdAt, updatedAt);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
