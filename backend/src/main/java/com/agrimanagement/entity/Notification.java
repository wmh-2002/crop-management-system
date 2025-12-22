package com.agrimanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    
    private String content;
    
    @Enumerated(EnumType.STRING)
    private NotificationType type; // FERTILIZING, WATERING, PESTICIDING, MONITORING, HARVESTING, OTHER
    
    @Enumerated(EnumType.STRING)
    private Priority priority = Priority.MEDIUM; // LOW, MEDIUM, HIGH
    
    @Column(name = "scheduled_time")
    private LocalDateTime scheduledTime;
    
    @Column(name = "sent_time")
    private LocalDateTime sentTime;
    
    @Enumerated(EnumType.STRING)
    private NotificationStatus status = NotificationStatus.PENDING; // PENDING, SENT, READ
    
    @Column(name = "recipient_ids")
    private String recipientIds; // JSON格式存储接收者ID列表
    
    @ManyToOne
    @JoinColumn(name = "plan_id")
    private PlantingPlan plan;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

enum NotificationType {
    FERTILIZING, WATERING, PESTICIDING, MONITORING, HARVESTING, OTHER
}

enum Priority {
    LOW, MEDIUM, HIGH
}

enum NotificationStatus {
    PENDING, SENT, READ
}