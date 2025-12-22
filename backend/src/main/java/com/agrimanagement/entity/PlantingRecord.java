package com.agrimanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "planting_records")
@Data
public class PlantingRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    private PlantingPlan plan;
    
    @Column(name = "operation_type")
    @Enumerated(EnumType.STRING)
    private OperationType operationType; // SOWING, FERTILIZING, WATERING, PESTICIDING, HARVESTING
    
    @Column(name = "operation_date")
    private LocalDate operationDate;
    
    @Column(name = "quantity_used")
    private BigDecimal quantityUsed;
    
    @ManyToOne
    @JoinColumn(name = "operator_id")
    private User operator;
    
    @Column(name = "weather_conditions")
    private String weatherConditions;
    
    private String notes;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

enum OperationType {
    SOWING, FERTILIZING, WATERING, PESTICIDING, HARVESTING
}