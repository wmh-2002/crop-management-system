package com.agricultural.management.dto;

import com.agricultural.management.entity.Farmland;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FarmlandUpdateRequest {
    private String name;

    @DecimalMin(value = "0.01", message = "面积必须大于0")
    @DecimalMax(value = "99999999.99", message = "面积不能超过99999999.99平方米")
    private BigDecimal area;

    private String location;

    private Farmland.FarmlandStatus status;

    private String description;

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getArea() {
        return area;
    }

    public void setArea(BigDecimal area) {
        this.area = area;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Farmland.FarmlandStatus getStatus() {
        return status;
    }

    public void setStatus(Farmland.FarmlandStatus status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
