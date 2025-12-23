package com.agricultural.management.repository;

import com.agricultural.management.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long>, JpaSpecificationExecutor<Notification> {

    List<Notification> findByType(Notification.NotificationType type);

    List<Notification> findByPriority(Notification.NotificationPriority priority);

    List<Notification> findByStatus(Notification.NotificationStatus status);

    List<Notification> findByPlanId(Long planId);

    List<Notification> findByRecipientIdsContaining(String recipientId);

    List<Notification> findByScheduledTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    List<Notification> findBySentTimeBetween(LocalDateTime startTime, LocalDateTime endTime);

    List<Notification> findByStatusAndScheduledTimeBefore(Notification.NotificationStatus status, LocalDateTime currentTime);

    List<Notification> findByRecipientIdsContainingAndStatus(String recipientId, Notification.NotificationStatus status);

    List<Notification> findByPlanIdAndType(Long planId, Notification.NotificationType type);

    List<Notification> findByTypeAndStatus(Notification.NotificationType type, Notification.NotificationStatus status);

    List<Notification> findByPriorityAndStatus(Notification.NotificationPriority priority, Notification.NotificationStatus status);
}
