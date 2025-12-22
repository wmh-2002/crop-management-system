package com.agrimanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "farmlands")
@Data
public class Farmland {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    
    private BigDecimal area; // 面积（平方米）
    
    private String location;
    
    @Enumerated(EnumType.STRING)
    private FarmlandStatus status = FarmlandStatus.AVAILABLE;
    
    private String description;
    
    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "farmland", cascade = CascadeType.ALL)
    private List<PlantingPlan> plantingPlans;
}

enum FarmlandStatus {
    AVAILABLE, PLANTED, MAINTENANCE
}