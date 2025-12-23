package com.agricultural.management.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationPriority priority;

    @Column(name = "scheduled_time", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime scheduledTime;

    @Column(name = "sent_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime sentTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationStatus status;

    @Column(name = "recipient_ids", columnDefinition = "JSON")
    private String recipientIds; // JSON字符串存储接收者ID列表

    @Column(name = "plan_id")
    private Long planId;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // 辅助字段，用于JSON序列化
    @Transient
    private List<Long> recipientIdList;

    public Notification() {
    }

    public Notification(Long id, String title, String content, NotificationType type,
                       NotificationPriority priority, LocalDateTime scheduledTime,
                       LocalDateTime sentTime, NotificationStatus status, String recipientIds,
                       Long planId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.type = type;
        this.priority = priority;
        this.scheduledTime = scheduledTime;
        this.sentTime = sentTime;
        this.status = status;
        this.recipientIds = recipientIds;
        this.planId = planId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public NotificationPriority getPriority() {
        return priority;
    }

    public void setPriority(NotificationPriority priority) {
        this.priority = priority;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public LocalDateTime getSentTime() {
        return sentTime;
    }

    public void setSentTime(LocalDateTime sentTime) {
        this.sentTime = sentTime;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public String getRecipientIds() {
        return recipientIds;
    }

    public void setRecipientIds(String recipientIds) {
        this.recipientIds = recipientIds;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<Long> getRecipientIdList() {
        return recipientIdList;
    }

    public void setRecipientIdList(List<Long> recipientIdList) {
        this.recipientIdList = recipientIdList;
    }

    public enum NotificationType {
        FERTILIZING,    // 施肥提醒
        WATERING,       // 灌溉提醒
        PESTICIDING,    // 施药提醒
        MONITORING,     // 监测提醒
        HARVESTING,     // 收获提醒
        OTHER           // 其他
    }

    public enum NotificationPriority {
        LOW,      // 低优先级
        MEDIUM,   // 中优先级
        HIGH      // 高优先级
    }

    public enum NotificationStatus {
        PENDING,  // 待发送
        SENT,     // 已发送
        READ      // 已阅读
    }

    public static class Builder {
        private Long id;
        private String title;
        private String content;
        private NotificationType type;
        private NotificationPriority priority;
        private LocalDateTime scheduledTime;
        private LocalDateTime sentTime;
        private NotificationStatus status;
        private String recipientIds;
        private Long planId;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder type(NotificationType type) {
            this.type = type;
            return this;
        }

        public Builder priority(NotificationPriority priority) {
            this.priority = priority;
            return this;
        }

        public Builder scheduledTime(LocalDateTime scheduledTime) {
            this.scheduledTime = scheduledTime;
            return this;
        }

        public Builder sentTime(LocalDateTime sentTime) {
            this.sentTime = sentTime;
            return this;
        }

        public Builder status(NotificationStatus status) {
            this.status = status;
            return this;
        }

        public Builder recipientIds(String recipientIds) {
            this.recipientIds = recipientIds;
            return this;
        }

        public Builder planId(Long planId) {
            this.planId = planId;
            return this;
        }

        public Builder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(LocalDateTime updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Notification build() {
            return new Notification(id, title, content, type, priority, scheduledTime,
                                  sentTime, status, recipientIds, planId, createdAt, updatedAt);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
