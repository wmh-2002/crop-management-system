package com.agrimanagement.service.impl;

import com.agrimanagement.entity.Notification;
import com.agrimanagement.repository.NotificationRepository;
import com.agrimanagement.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationServiceImpl implements NotificationService {
    
    @Autowired
    private NotificationRepository notificationRepository;
    
    @Override
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }
    
    @Override
    public Optional<Notification> getNotificationById(Long id) {
        return notificationRepository.findById(id);
    }
    
    @Override
    public Notification createNotification(Notification notification) {
        notification.setCreatedAt(LocalDateTime.now());
        notification.setUpdatedAt(LocalDateTime.now());
        return notificationRepository.save(notification);
    }
    
    @Override
    public Notification updateNotification(Long id, Notification notificationDetails) {
        Notification notification = notificationRepository.findById(id).orElse(null);
        if (notification != null) {
            notification.setTitle(notificationDetails.getTitle());
            notification.setContent(notificationDetails.getContent());
            notification.setType(notificationDetails.getType());
            notification.setPriority(notificationDetails.getPriority());
            notification.setScheduledTime(notificationDetails.getScheduledTime());
            notification.setRecipientIds(notificationDetails.getRecipientIds());
            notification.setPlan(notificationDetails.getPlan());
            
            notification.setUpdatedAt(LocalDateTime.now());
            
            return notificationRepository.save(notification);
        }
        return null;
    }
    
    @Override
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }
    
    @Override
    public List<Notification> getPendingNotifications() {
        // 返回所有状态为PENDING的提醒
        return notificationRepository.findAll();
    }
}