package com.agricultural.management.controller;

import com.agricultural.management.dto.*;
import com.agricultural.management.entity.Notification;
import com.agricultural.management.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*", maxAge = 3600)
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * 获取通知列表（分页）
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<PageResponse<NotificationResponse>>> getNotificationList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long planId,
            @RequestParam(required = false) Long recipientId,
            @RequestParam(required = false) String scheduledTimeFrom,
            @RequestParam(required = false) String scheduledTimeTo,
            @RequestParam(required = false) String sentTimeFrom,
            @RequestParam(required = false) String sentTimeTo) {

        try {
            NotificationQueryRequest request = new NotificationQueryRequest();
            request.setPage(page);
            request.setSize(size);
            request.setTitle(title);
            request.setPlanId(planId);
            request.setRecipientId(recipientId);

            // 转换枚举字符串为枚举值
            if (type != null && !type.trim().isEmpty()) {
                try {
                    request.setType(Notification.NotificationType.valueOf(type.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid type: " + type);
                }
            }

            if (priority != null && !priority.trim().isEmpty()) {
                try {
                    request.setPriority(Notification.NotificationPriority.valueOf(priority.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid priority: " + priority);
                }
            }

            if (status != null && !status.trim().isEmpty()) {
                try {
                    request.setStatus(Notification.NotificationStatus.valueOf(status.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid status: " + status);
                }
            }

            // 转换时间字符串为LocalDateTime
            if (scheduledTimeFrom != null && !scheduledTimeFrom.trim().isEmpty()) {
                try {
                    request.setScheduledTimeFrom(LocalDateTime.parse(scheduledTimeFrom.replace(" ", "T")));
                } catch (Exception e) {
                    System.out.println("Invalid scheduledTimeFrom: " + scheduledTimeFrom);
                }
            }

            if (scheduledTimeTo != null && !scheduledTimeTo.trim().isEmpty()) {
                try {
                    request.setScheduledTimeTo(LocalDateTime.parse(scheduledTimeTo.replace(" ", "T")));
                } catch (Exception e) {
                    System.out.println("Invalid scheduledTimeTo: " + scheduledTimeTo);
                }
            }

            if (sentTimeFrom != null && !sentTimeFrom.trim().isEmpty()) {
                try {
                    request.setSentTimeFrom(LocalDateTime.parse(sentTimeFrom.replace(" ", "T")));
                } catch (Exception e) {
                    System.out.println("Invalid sentTimeFrom: " + sentTimeFrom);
                }
            }

            if (sentTimeTo != null && !sentTimeTo.trim().isEmpty()) {
                try {
                    request.setSentTimeTo(LocalDateTime.parse(sentTimeTo.replace(" ", "T")));
                } catch (Exception e) {
                    System.out.println("Invalid sentTimeTo: " + sentTimeTo);
                }
            }

            PageResponse<NotificationResponse> result = notificationService.getNotificationList(request);
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            System.err.println("Error in getNotificationList: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<PageResponse<NotificationResponse>>error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 根据ID获取通知信息
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<NotificationResponse>> getNotificationById(@PathVariable Long id) {
        try {
            NotificationResponse notification = notificationService.getNotificationById(id);
            return ResponseEntity.ok(ApiResponse.success(notification));
        } catch (Exception e) {
            System.err.println("Error in getNotificationById: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<NotificationResponse>error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 创建新通知
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<NotificationResponse>> createNotification(@Valid @RequestBody NotificationCreateRequest request) {
        try {
            NotificationResponse newNotification = notificationService.createNotification(request);
            return ResponseEntity.status(201).body(ApiResponse.success("通知创建成功", newNotification));
        } catch (Exception e) {
            System.err.println("Error in createNotification: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<NotificationResponse>error("创建通知失败: " + e.getMessage()));
        }
    }

    /**
     * 更新通知信息
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<NotificationResponse>> updateNotification(@PathVariable Long id, @Valid @RequestBody NotificationUpdateRequest request) {
        try {
            NotificationResponse updatedNotification = notificationService.updateNotification(id, request);
            return ResponseEntity.ok(ApiResponse.success("通知更新成功", updatedNotification));
        } catch (Exception e) {
            System.err.println("Error in updateNotification: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<NotificationResponse>error("更新通知失败: " + e.getMessage()));
        }
    }

    /**
     * 删除通知
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<String>> deleteNotification(@PathVariable Long id) {
        try {
            notificationService.deleteNotification(id);
            return ResponseEntity.ok(ApiResponse.success("通知删除成功"));
        } catch (Exception e) {
            System.err.println("Error in deleteNotification: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<String>error("删除通知失败: " + e.getMessage()));
        }
    }

    /**
     * 发送通知
     */
    @PostMapping("/{id}/send")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<NotificationResponse>> sendNotification(@PathVariable Long id) {
        try {
            NotificationResponse sentNotification = notificationService.sendNotification(id);
            return ResponseEntity.ok(ApiResponse.success("通知发送成功", sentNotification));
        } catch (Exception e) {
            System.err.println("Error in sendNotification: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<NotificationResponse>error("发送通知失败: " + e.getMessage()));
        }
    }

    /**
     * 标记通知为已读
     */
    @PostMapping("/{id}/read")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<NotificationResponse>> markAsRead(@PathVariable Long id) {
        try {
            NotificationResponse readNotification = notificationService.markAsRead(id);
            return ResponseEntity.ok(ApiResponse.success("通知已标记为已读", readNotification));
        } catch (Exception e) {
            System.err.println("Error in markAsRead: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<NotificationResponse>error("标记已读失败: " + e.getMessage()));
        }
    }

    /**
     * 批量发送通知
     */
    @PostMapping("/batch-send")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> sendNotifications(@RequestBody List<Long> notificationIds) {
        try {
            List<NotificationResponse> sentNotifications = notificationService.sendNotifications(notificationIds);
            return ResponseEntity.ok(ApiResponse.success("批量发送通知成功", sentNotifications));
        } catch (Exception e) {
            System.err.println("Error in sendNotifications: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<List<NotificationResponse>>error("批量发送通知失败: " + e.getMessage()));
        }
    }

    /**
     * 根据种植计划获取通知列表
     */
    @GetMapping("/plan/{planId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getNotificationsByPlan(@PathVariable Long planId) {
        try {
            List<NotificationResponse> notifications = notificationService.getNotificationsByPlan(planId);
            return ResponseEntity.ok(ApiResponse.success(notifications));
        } catch (Exception e) {
            System.err.println("Error in getNotificationsByPlan: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<List<NotificationResponse>>error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 根据类型获取通知列表
     */
    @GetMapping("/type/{type}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getNotificationsByType(@PathVariable String type) {
        try {
            Notification.NotificationType notificationType = Notification.NotificationType.valueOf(type.toUpperCase());
            List<NotificationResponse> notifications = notificationService.getNotificationsByType(notificationType);
            return ResponseEntity.ok(ApiResponse.success(notifications));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.<List<NotificationResponse>>error("无效的通知类型: " + type));
        } catch (Exception e) {
            System.err.println("Error in getNotificationsByType: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<List<NotificationResponse>>error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 根据优先级获取通知列表
     */
    @GetMapping("/priority/{priority}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getNotificationsByPriority(@PathVariable String priority) {
        try {
            Notification.NotificationPriority notificationPriority = Notification.NotificationPriority.valueOf(priority.toUpperCase());
            List<NotificationResponse> notifications = notificationService.getNotificationsByPriority(notificationPriority);
            return ResponseEntity.ok(ApiResponse.success(notifications));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.<List<NotificationResponse>>error("无效的优先级: " + priority));
        } catch (Exception e) {
            System.err.println("Error in getNotificationsByPriority: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<List<NotificationResponse>>error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 根据状态获取通知列表
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getNotificationsByStatus(@PathVariable String status) {
        try {
            Notification.NotificationStatus notificationStatus = Notification.NotificationStatus.valueOf(status.toUpperCase());
            List<NotificationResponse> notifications = notificationService.getNotificationsByStatus(notificationStatus);
            return ResponseEntity.ok(ApiResponse.success(notifications));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.<List<NotificationResponse>>error("无效的状态: " + status));
        } catch (Exception e) {
            System.err.println("Error in getNotificationsByStatus: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<List<NotificationResponse>>error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 根据接收者获取通知列表
     */
    @GetMapping("/recipient/{recipientId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getNotificationsByRecipient(@PathVariable Long recipientId) {
        try {
            List<NotificationResponse> notifications = notificationService.getNotificationsByRecipient(recipientId);
            return ResponseEntity.ok(ApiResponse.success(notifications));
        } catch (Exception e) {
            System.err.println("Error in getNotificationsByRecipient: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<List<NotificationResponse>>error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 根据接收者和状态获取通知列表
     */
    @GetMapping("/recipient/{recipientId}/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getNotificationsByRecipientAndStatus(
            @PathVariable Long recipientId, @PathVariable String status) {
        try {
            Notification.NotificationStatus notificationStatus = Notification.NotificationStatus.valueOf(status.toUpperCase());
            List<NotificationResponse> notifications = notificationService.getNotificationsByRecipientAndStatus(recipientId, notificationStatus);
            return ResponseEntity.ok(ApiResponse.success(notifications));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.<List<NotificationResponse>>error("无效的状态: " + status));
        } catch (Exception e) {
            System.err.println("Error in getNotificationsByRecipientAndStatus: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<List<NotificationResponse>>error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 获取待发送的通知列表
     */
    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getPendingNotifications() {
        try {
            List<NotificationResponse> notifications = notificationService.getPendingNotifications();
            return ResponseEntity.ok(ApiResponse.success(notifications));
        } catch (Exception e) {
            System.err.println("Error in getPendingNotifications: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<List<NotificationResponse>>error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 获取待发送的通知列表（计划时间已到）
     */
    @GetMapping("/to-send")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getNotificationsToSend() {
        try {
            List<NotificationResponse> notifications = notificationService.getNotificationsToSend();
            return ResponseEntity.ok(ApiResponse.success(notifications));
        } catch (Exception e) {
            System.err.println("Error in getNotificationsToSend: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<List<NotificationResponse>>error("查询失败: " + e.getMessage()));
        }
    }
}
