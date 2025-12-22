package com.agrimanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "planting_plans")
@Data
public class PlantingPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "farmland_id", nullable = false)
    private Farmland farmland;
    
    @ManyToOne
    @JoinColumn(name = "crop_id", nullable = false)
    private Crop crop;
    
    @Column(name = "plan_name")
    private String planName;
    
    @Column(name = "planned_start_date")
    private LocalDate plannedStartDate;
    
    @Column(name = "planned_end_date")
    private LocalDate plannedEndDate;
    
    @Column(name = "expected_harvest_date")
    private LocalDate expectedHarvestDate;
    
    @Column(name = "sowing_density")
    private String sowingDensity;
    
    private String notes;
    
    @Enumerated(EnumType.STRING)
    private PlanStatus status = PlanStatus.PLANNED;
    
    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL)
    private List<PlantingRecord> plantingRecords;
    
    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL)
    private List<GrowthMonitoring> growthMonitorings;
    
    @OneToMany(mappedBy = "plan", cascade = CascadeType.ALL)
    private List<Notification> notifications;
}

enum PlanStatus {
    PLANNED, IN_PROGRESS, COMPLETED, CANCELLED
}