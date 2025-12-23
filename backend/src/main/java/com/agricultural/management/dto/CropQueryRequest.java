package com.agricultural.management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CropQueryRequest {
    private Integer page = 1;
    private Integer size = 10;
    private String name;
    private String variety;
    private String plantingSeason;
    private Integer minGrowthPeriod;
    private Integer maxGrowthPeriod;

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

    public Integer getMinGrowthPeriod() {
        return minGrowthPeriod;
    }

    public void setMinGrowthPeriod(Integer minGrowthPeriod) {
        this.minGrowthPeriod = minGrowthPeriod;
    }

    public Integer getMaxGrowthPeriod() {
        return maxGrowthPeriod;
    }

    public void setMaxGrowthPeriod(Integer maxGrowthPeriod) {
        this.maxGrowthPeriod = maxGrowthPeriod;
    }
}
