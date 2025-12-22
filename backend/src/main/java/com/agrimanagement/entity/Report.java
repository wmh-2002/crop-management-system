package com.agrimanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
@Data
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    
    @Column(name = "report_type")
    @Enumerated(EnumType.STRING)
    private ReportType reportType; // CROP_GROWTH, FIELD_UTILIZATION, PRODUCTION_ANALYSIS, WEATHER_IMPACT, PREDICTIVE_ANALYSIS
    
    @Column(name = "start_date")
    private LocalDate startDate;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    private String content;
    
    @Column(name = "chart_data")
    private String chartData; // JSON格式存储图表数据
    
    @ManyToOne
    @JoinColumn(name = "generated_by")
    private User generatedBy;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

enum ReportType {
    CROP_GROWTH, FIELD_UTILIZATION, PRODUCTION_ANALYSIS, WEATHER_IMPACT, PREDICTIVE_ANALYSIS
}