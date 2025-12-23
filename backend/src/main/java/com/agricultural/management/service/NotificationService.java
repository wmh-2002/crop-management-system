package com.agricultural.management.service;

import com.agricultural.management.dto.*;
import com.agricultural.management.entity.Notification;
import com.agricultural.management.entity.PlantingPlan;
import com.agricultural.management.entity.User;
import com.agricultural.management.exception.ResourceNotFoundException;
import com.agricultural.management.repository.NotificationRepository;
import com.agricultural.management.repository.PlantingPlanRepository;
import com.agricultural.management.repository.UserRepository;
import com.agricultural.management.security.UserPrincipal;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final PlantingPlanRepository plantingPlanRepository;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public NotificationService(NotificationRepository notificationRepository,
                             PlantingPlanRepository plantingPlanRepository,
                             UserRepository userRepository,
                             ObjectMapper objectMapper) {
        this.notificationRepository = notificationRepository;
        this.plantingPlanRepository = plantingPlanRepository;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    /**
     * 分页查询通知列表
     */
    public PageResponse<NotificationResponse> getNotificationList(NotificationQueryRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());

        Specification<Notification> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getTitle() != null && !request.getTitle().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("title"),
                    "%" + request.getTitle().trim() + "%"));
            }

            if (request.getType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("type"), request.getType()));
            }

            if (request.getPriority() != null) {
                predicates.add(criteriaBuilder.equal(root.get("priority"), request.getPriority()));
            }

            if (request.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), request.getStatus()));
            }

            if (request.getPlanId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("planId"), request.getPlanId()));
            }

            if (request.getRecipientId() != null) {
                predicates.add(criteriaBuilder.like(root.get("recipientIds"),
                    "%" + request.getRecipientId() + "%"));
            }

            if (request.getScheduledTimeFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("scheduledTime"), request.getScheduledTimeFrom()));
            }

            if (request.getScheduledTimeTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("scheduledTime"), request.getScheduledTimeTo()));
            }

            if (request.getSentTimeFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("sentTime"), request.getSentTimeFrom()));
            }

            if (request.getSentTimeTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("sentTime"), request.getSentTimeTo()));
            }

            if (predicates.isEmpty()) {
                return null; // 没有查询条件，返回所有数据
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<Notification> notificationPage = notificationRepository.findAll(spec, pageable);

        List<NotificationResponse> notificationResponses = notificationPage.getContent().stream()
            .map(this::convertToNotificationResponse)
            .collect(Collectors.toList());

        return PageResponse.<NotificationResponse>builder()
            .content(notificationResponses)
            .page(notificationPage.getNumber() + 1)
            .size(notificationPage.getSize())
            .totalElements(notificationPage.getTotalElements())
            .totalPages(notificationPage.getTotalPages())
            .last(notificationPage.isLast())
            .build();
    }

    /**
     * 根据ID获取通知信息
     */
    public NotificationResponse getNotificationById(Long id) {
        Notification notification = notificationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("通知不存在，ID: " + id));
        return convertToNotificationResponse(notification);
    }

    /**
     * 创建新通知
     */
    @Transactional
    public NotificationResponse createNotification(NotificationCreateRequest request) {
        // 验证种植计划存在性（如果指定了planId）
        if (request.getPlanId() != null) {
            plantingPlanRepository.findById(request.getPlanId())
                .orElseThrow(() -> new ResourceNotFoundException("种植计划不存在，ID: " + request.getPlanId()));
        }

        // 验证接收者ID列表中的用户都存在
        if (request.getRecipientIds() != null && !request.getRecipientIds().isEmpty()) {
            for (Long recipientId : request.getRecipientIds()) {
                userRepository.findById(recipientId)
                    .orElseThrow(() -> new ResourceNotFoundException("接收者用户不存在，ID: " + recipientId));
            }
        }

        // 验证计划发送时间不能是过去的时间（如果是PENDING状态）
        if (request.getStatus() == Notification.NotificationStatus.PENDING &&
            request.getScheduledTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("计划发送时间不能是过去的时间");
        }

        Notification notification = new Notification();
        notification.setTitle(request.getTitle());
        notification.setContent(request.getContent());
        notification.setType(request.getType());
        notification.setPriority(request.getPriority());
        notification.setScheduledTime(request.getScheduledTime());
        notification.setStatus(request.getStatus());
        notification.setPlanId(request.getPlanId());

        // 将接收者ID列表转换为JSON字符串
        if (request.getRecipientIds() != null && !request.getRecipientIds().isEmpty()) {
            try {
                notification.setRecipientIds(objectMapper.writeValueAsString(request.getRecipientIds()));
            } catch (Exception e) {
                throw new RuntimeException("接收者ID列表序列化失败", e);
            }
        }

        // 如果状态是SENT，设置发送时间
        if (request.getStatus() == Notification.NotificationStatus.SENT) {
            notification.setSentTime(LocalDateTime.now());
        }

        Notification savedNotification = notificationRepository.save(notification);
        return convertToNotificationResponse(savedNotification);
    }

    /**
     * 更新通知信息
     */
    @Transactional
    public NotificationResponse updateNotification(Long id, NotificationUpdateRequest request) {
        Notification notification = notificationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("通知不存在，ID: " + id));

        // 验证种植计划存在性（如果更新了planId）
        if (request.getPlanId() != null && !request.getPlanId().equals(notification.getPlanId())) {
            plantingPlanRepository.findById(request.getPlanId())
                .orElseThrow(() -> new ResourceNotFoundException("种植计划不存在，ID: " + request.getPlanId()));
        }

        // 验证接收者ID列表中的用户都存在（如果更新了接收者列表）
        if (request.getRecipientIds() != null && !request.getRecipientIds().isEmpty()) {
            for (Long recipientId : request.getRecipientIds()) {
                userRepository.findById(recipientId)
                    .orElseThrow(() -> new ResourceNotFoundException("接收者用户不存在，ID: " + recipientId));
            }
        }

        // 验证计划发送时间不能是过去的时间（如果是PENDING状态）
        if (request.getScheduledTime() != null && request.getStatus() != null &&
            request.getStatus() == Notification.NotificationStatus.PENDING &&
            request.getScheduledTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("计划发送时间不能是过去的时间");
        }

        // 更新字段
        if (request.getTitle() != null) {
            notification.setTitle(request.getTitle());
        }
        if (request.getContent() != null) {
            notification.setContent(request.getContent());
        }
        if (request.getType() != null) {
            notification.setType(request.getType());
        }
        if (request.getPriority() != null) {
            notification.setPriority(request.getPriority());
        }
        if (request.getScheduledTime() != null) {
            notification.setScheduledTime(request.getScheduledTime());
        }
        if (request.getStatus() != null) {
            notification.setStatus(request.getStatus());

            // 如果状态更新为SENT且之前不是SENT，设置发送时间
            if (request.getStatus() == Notification.NotificationStatus.SENT &&
                notification.getSentTime() == null) {
                notification.setSentTime(LocalDateTime.now());
            }
        }
        if (request.getPlanId() != null) {
            notification.setPlanId(request.getPlanId());
        }

        // 更新接收者ID列表
        if (request.getRecipientIds() != null) {
            try {
                notification.setRecipientIds(objectMapper.writeValueAsString(request.getRecipientIds()));
            } catch (Exception e) {
                throw new RuntimeException("接收者ID列表序列化失败", e);
            }
        }

        Notification updatedNotification = notificationRepository.save(notification);
        return convertToNotificationResponse(updatedNotification);
    }

    /**
     * 删除通知
     */
    @Transactional
    public void deleteNotification(Long id) {
        Notification notification = notificationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("通知不存在，ID: " + id));

        notificationRepository.delete(notification);
    }

    /**
     * 发送通知（将状态从PENDING更新为SENT）
     */
    @Transactional
    public NotificationResponse sendNotification(Long id) {
        Notification notification = notificationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("通知不存在，ID: " + id));

        if (notification.getStatus() != Notification.NotificationStatus.PENDING) {
            throw new IllegalArgumentException("只有待发送状态的通知才能发送");
        }

        notification.setStatus(Notification.NotificationStatus.SENT);
        notification.setSentTime(LocalDateTime.now());

        Notification sentNotification = notificationRepository.save(notification);
        return convertToNotificationResponse(sentNotification);
    }

    /**
     * 标记通知为已读
     */
    @Transactional
    public NotificationResponse markAsRead(Long id) {
        Notification notification = notificationRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("通知不存在，ID: " + id));

        notification.setStatus(Notification.NotificationStatus.READ);

        Notification readNotification = notificationRepository.save(notification);
        return convertToNotificationResponse(readNotification);
    }

    /**
     * 获取指定种植计划的通知列表
     */
    public List<NotificationResponse> getNotificationsByPlan(Long planId) {
        // 验证种植计划存在
        plantingPlanRepository.findById(planId)
            .orElseThrow(() -> new ResourceNotFoundException("种植计划不存在，ID: " + planId));

        List<Notification> notifications = notificationRepository.findByPlanId(planId);
        return notifications.stream()
            .map(this::convertToNotificationResponse)
            .collect(Collectors.toList());
    }

    /**
     * 获取指定类型的通知列表
     */
    public List<NotificationResponse> getNotificationsByType(Notification.NotificationType type) {
        List<Notification> notifications = notificationRepository.findByType(type);
        return notifications.stream()
            .map(this::convertToNotificationResponse)
            .collect(Collectors.toList());
    }

    /**
     * 获取指定优先级的通知列表
     */
    public List<NotificationResponse> getNotificationsByPriority(Notification.NotificationPriority priority) {
        List<Notification> notifications = notificationRepository.findByPriority(priority);
        return notifications.stream()
            .map(this::convertToNotificationResponse)
            .collect(Collectors.toList());
    }

    /**
     * 获取指定状态的通知列表
     */
    public List<NotificationResponse> getNotificationsByStatus(Notification.NotificationStatus status) {
        List<Notification> notifications = notificationRepository.findByStatus(status);
        return notifications.stream()
            .map(this::convertToNotificationResponse)
            .collect(Collectors.toList());
    }

    /**
     * 获取指定接收者的通知列表
     */
    public List<NotificationResponse> getNotificationsByRecipient(Long recipientId) {
        // 验证用户存在
        userRepository.findById(recipientId)
            .orElseThrow(() -> new ResourceNotFoundException("用户不存在，ID: " + recipientId));

        List<Notification> notifications = notificationRepository.findByRecipientIdsContaining(recipientId.toString());
        return notifications.stream()
            .map(this::convertToNotificationResponse)
            .collect(Collectors.toList());
    }

    /**
     * 获取指定接收者和状态的通知列表
     */
    public List<NotificationResponse> getNotificationsByRecipientAndStatus(Long recipientId, Notification.NotificationStatus status) {
        // 验证用户存在
        userRepository.findById(recipientId)
            .orElseThrow(() -> new ResourceNotFoundException("用户不存在，ID: " + recipientId));

        List<Notification> notifications = notificationRepository.findByRecipientIdsContainingAndStatus(recipientId.toString(), status);
        return notifications.stream()
            .map(this::convertToNotificationResponse)
            .collect(Collectors.toList());
    }

    /**
     * 获取待发送的通知列表
     */
    public List<NotificationResponse> getPendingNotifications() {
        List<Notification> notifications = notificationRepository.findByStatus(Notification.NotificationStatus.PENDING);
        return notifications.stream()
            .map(this::convertToNotificationResponse)
            .collect(Collectors.toList());
    }

    /**
     * 获取应该发送的通知（计划时间已到且状态为PENDING）
     */
    public List<NotificationResponse> getNotificationsToSend() {
        List<Notification> notifications = notificationRepository
            .findByStatusAndScheduledTimeBefore(Notification.NotificationStatus.PENDING, LocalDateTime.now());
        return notifications.stream()
            .map(this::convertToNotificationResponse)
            .collect(Collectors.toList());
    }

    /**
     * 批量发送通知
     */
    @Transactional
    public List<NotificationResponse> sendNotifications(List<Long> notificationIds) {
        List<Notification> notifications = notificationRepository.findAllById(notificationIds);

        for (Notification notification : notifications) {
            if (notification.getStatus() == Notification.NotificationStatus.PENDING) {
                notification.setStatus(Notification.NotificationStatus.SENT);
                notification.setSentTime(LocalDateTime.now());
            }
        }

        List<Notification> sentNotifications = notificationRepository.saveAll(notifications);
        return sentNotifications.stream()
            .map(this::convertToNotificationResponse)
            .collect(Collectors.toList());
    }

    /**
     * 获取当前用户ID
     */
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return userPrincipal.getId();
        }
        throw new RuntimeException("无法获取当前用户信息");
    }

    private NotificationResponse convertToNotificationResponse(Notification notification) {
        List<Long> recipientIds = null;
        if (notification.getRecipientIds() != null && !notification.getRecipientIds().trim().isEmpty()) {
            try {
                recipientIds = objectMapper.readValue(notification.getRecipientIds(),
                    new TypeReference<List<Long>>() {});
            } catch (Exception e) {
                // JSON解析失败，忽略
                recipientIds = new ArrayList<>();
            }
        }

        return new NotificationResponse(
            notification.getId(),
            notification.getTitle(),
            notification.getContent(),
            notification.getType(),
            notification.getPriority(),
            notification.getScheduledTime(),
            notification.getSentTime(),
            notification.getStatus(),
            recipientIds,
            notification.getPlanId(),
            notification.getCreatedAt(),
            notification.getUpdatedAt()
        );
    }
}
