package com.agricultural.management.dto;

import com.agricultural.management.entity.Notification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationQueryRequest {
    private Integer page = 1;
    private Integer size = 10;
    private String title;
    private Notification.NotificationType type;
    private Notification.NotificationPriority priority;
    private Notification.NotificationStatus status;
    private Long planId;
    private Long recipientId;
    private LocalDateTime scheduledTimeFrom;
    private LocalDateTime scheduledTimeTo;
    private LocalDateTime sentTimeFrom;
    private LocalDateTime sentTimeTo;

    // Getters and Setters
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Notification.NotificationType getType() {
        return type;
    }

    public void setType(Notification.NotificationType type) {
        this.type = type;
    }

    public Notification.NotificationPriority getPriority() {
        return priority;
    }

    public void setPriority(Notification.NotificationPriority priority) {
        this.priority = priority;
    }

    public Notification.NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(Notification.NotificationStatus status) {
        this.status = status;
    }

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public Long getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(Long recipientId) {
        this.recipientId = recipientId;
    }

    public LocalDateTime getScheduledTimeFrom() {
        return scheduledTimeFrom;
    }

    public void setScheduledTimeFrom(LocalDateTime scheduledTimeFrom) {
        this.scheduledTimeFrom = scheduledTimeFrom;
    }

    public LocalDateTime getScheduledTimeTo() {
        return scheduledTimeTo;
    }

    public void setScheduledTimeTo(LocalDateTime scheduledTimeTo) {
        this.scheduledTimeTo = scheduledTimeTo;
    }

    public LocalDateTime getSentTimeFrom() {
        return sentTimeFrom;
    }

    public void setSentTimeFrom(LocalDateTime sentTimeFrom) {
        this.sentTimeFrom = sentTimeFrom;
    }

    public LocalDateTime getSentTimeTo() {
        return sentTimeTo;
    }

    public void setSentTimeTo(LocalDateTime sentTimeTo) {
        this.sentTimeTo = sentTimeTo;
    }
}
