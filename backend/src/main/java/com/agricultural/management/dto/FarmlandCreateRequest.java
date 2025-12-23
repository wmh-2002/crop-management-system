package com.agricultural.management.dto;

import com.agricultural.management.entity.Farmland;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FarmlandCreateRequest {
    @NotBlank(message = "农田名称不能为空")
    private String name;

    @NotNull(message = "面积不能为空")
    @DecimalMin(value = "0.01", message = "面积必须大于0")
    @DecimalMax(value = "99999999.99", message = "面积不能超过99999999.99平方米")
    private BigDecimal area;

    private String location;

    @NotNull(message = "农田状态不能为空")
    private Farmland.FarmlandStatus status = Farmland.FarmlandStatus.AVAILABLE;

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
