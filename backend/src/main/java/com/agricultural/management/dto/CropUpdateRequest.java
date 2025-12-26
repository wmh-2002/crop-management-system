package com.agricultural.management.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CropUpdateRequest {
    private String name;

    private String variety;

    private String cropCategory;

    private String plantingSeason;

    @Min(value = 1, message = "生长期必须大于0天")
    private Integer growthPeriod;

    @DecimalMin(value = "0.01", message = "预期产量必须大于0")
    @DecimalMax(value = "99999999.99", message = "预期产量不能超过99999999.99")
    private BigDecimal expectedYield;

    private String waterNeeds;

    private String fertilizerNeeds;

    private String diseaseInfo;

    private String description;

    // Getters and Setters
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
}
