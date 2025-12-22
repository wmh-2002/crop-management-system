package com.agrimanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String username;
    
    @JsonIgnore // 不在API返回中显示密码
    private String password;
    
    @Column(name = "real_name", nullable = false)
    private String realName;
    
    private String email;
    
    private String phone;
    
    @Enumerated(EnumType.STRING)
    private UserRole role; // ADMIN, FARMER, STAFF
    
    private Integer status = 1; // 1-启用，0-禁用
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL)
    private List<Farmland> farmlands;
    
    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL)
    private List<PlantingPlan> plantingPlans;
    
    @OneToMany(mappedBy = "operator", cascade = CascadeType.ALL)
    private List<PlantingRecord> plantingRecords;
    
    @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL)
    private List<GrowthMonitoring> growthMonitorings;
}

// 用户角色枚举
enum UserRole {
    ADMIN, FARMER, STAFF
}