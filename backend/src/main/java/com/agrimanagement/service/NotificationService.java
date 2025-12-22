package com.agrimanagement.service;

import com.agrimanagement.entity.Notification;
import java.util.List;
import java.util.Optional;

public interface NotificationService {
    List<Notification> getAllNotifications();
    Optional<Notification> getNotificationById(Long id);
    Notification createNotification(Notification notification);
    Notification updateNotification(Long id, Notification notificationDetails);
    void deleteNotification(Long id);
    List<Notification> getPendingNotifications();
}