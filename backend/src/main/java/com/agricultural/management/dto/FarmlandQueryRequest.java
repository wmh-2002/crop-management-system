package com.agricultural.management.dto;

import com.agricultural.management.entity.Farmland;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FarmlandQueryRequest {
    private Integer page = 1;
    private Integer size = 10;
    private String name;
    private String location;
    private Farmland.FarmlandStatus status;
    private Long createdBy;

    // Getter and Setter methods
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

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }
}
