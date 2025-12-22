package com.agrimanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "growth_monitoring")
@Data
public class GrowthMonitoring {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "plan_id", nullable = false)
    private PlantingPlan plan;
    
    @Column(name = "monitoring_date")
    private LocalDate monitoringDate;
    
    @Column(name = "height_measurement")
    private BigDecimal heightMeasurement; // 高度测量（厘米）
    
    @Column(name = "width_measurement")
    private BigDecimal widthMeasurement; // 宽度测量（厘米）
    
    @Column(name = "health_status")
    @Enumerated(EnumType.STRING)
    private HealthStatus healthStatus; // EXCELLENT, GOOD, FAIR, POOR
    
    @Column(name = "soil_moisture")
    private BigDecimal soilMoisture; // 土壤湿度（%）
    
    private BigDecimal temperature; // 温度（摄氏度）
    
    private BigDecimal humidity; // 湿度（%）
    
    @Column(name = "light_intensity")
    private Integer lightIntensity; // 光照强度（lux）
    
    @Column(name = "ph_level")
    private BigDecimal phLevel; // pH值
    
    private String notes;
    
    @Column(name = "photo_url")
    private String photoUrl;
    
    @ManyToOne
    @JoinColumn(name = "created_by")
    private User createdBy;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

enum HealthStatus {
    EXCELLENT, GOOD, FAIR, POOR
}