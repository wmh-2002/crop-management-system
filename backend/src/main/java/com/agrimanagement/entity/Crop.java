package com.agrimanagement.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "crops")
@Data
public class Crop {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    private String variety;
    
    @Column(name = "planting_season")
    private String plantingSeason;
    
    @Column(name = "growth_period")
    private Integer growthPeriod; // 生长期（天）
    
    @Column(name = "expected_yield")
    private BigDecimal expectedYield;
    
    @Column(name = "water_needs")
    private String waterNeeds;
    
    @Column(name = "fertilizer_needs")
    private String fertilizerNeeds;
    
    @Column(name = "disease_info")
    private String diseaseInfo;
    
    private String description;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "crop", cascade = CascadeType.ALL)
    private List<PlantingPlan> plantingPlans;
}