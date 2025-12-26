package com.agricultural.management.service;

import com.agricultural.management.dto.DashboardResponse;
import com.agricultural.management.entity.*;
import com.agricultural.management.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final UserRepository userRepository;
    private final CropRepository cropRepository;
    private final FarmlandRepository farmlandRepository;
    private final PlantingPlanRepository plantingPlanRepository;
    private final NotificationRepository notificationRepository;

    public DashboardService(UserRepository userRepository,
                           CropRepository cropRepository,
                           FarmlandRepository farmlandRepository,
                           PlantingPlanRepository plantingPlanRepository,
                           NotificationRepository notificationRepository) {
        this.userRepository = userRepository;
        this.cropRepository = cropRepository;
        this.farmlandRepository = farmlandRepository;
        this.plantingPlanRepository = plantingPlanRepository;
        this.notificationRepository = notificationRepository;
    }

    /**
     * 获取仪表板统计数据
     */
    public DashboardResponse getDashboardData() {
        return DashboardResponse.builder()
            .userCount(getUserCount())
            .cropCount(getCropCount())
            .farmlandCount(getFarmlandCount())
            .planCount(getPlanCount())
            .farmlandStatusData(getFarmlandStatusData())
            .cropCategoryData(getCropCategoryData())
            .planStatusData(getPlanStatusData())
            .productionTrendData(getProductionTrendData())
            .recentNotifications(getRecentNotifications())
            .build();
    }

    /**
     * 获取用户总数
     */
    private Long getUserCount() {
        return userRepository.count();
    }

    /**
     * 获取作物种类总数
     */
    private Long getCropCount() {
        return cropRepository.count();
    }

    /**
     * 获取农田总数
     */
    private Long getFarmlandCount() {
        return farmlandRepository.count();
    }

    /**
     * 获取种植计划总数
     */
    private Long getPlanCount() {
        return plantingPlanRepository.count();
    }

    /**
     * 获取农田状态分布数据
     */
    private List<Map<String, Object>> getFarmlandStatusData() {
        List<Object[]> results = farmlandRepository.countByStatus();
        return results.stream()
            .map(result -> {
                Map<String, Object> map = new HashMap<>();
                // result[0] 是 FarmlandStatus 枚举，需要先转换为枚举类型
                Farmland.FarmlandStatus status = (Farmland.FarmlandStatus) result[0];
                map.put("name", getFarmlandStatusDisplayName(status.name()));
                map.put("value", result[1]);
                return map;
            })
            .collect(Collectors.toList());
    }

    /**
     * 获取作物种类分布数据
     */
    private List<Map<String, Object>> getCropCategoryData() {
        List<Object[]> results = cropRepository.countByCropCategory();
        return results.stream()
            .map(result -> {
                Map<String, Object> map = new HashMap<>();
                // result[0] 是 CropCategory 枚举，需要先转换为枚举类型
                Crop.CropCategory category = (Crop.CropCategory) result[0];
                map.put("name", getCropCategoryDisplayName(category.name()));
                map.put("value", result[1]);
                return map;
            })
            .collect(Collectors.toList());
    }

    /**
     * 获取种植计划状态分布数据
     */
    private List<Map<String, Object>> getPlanStatusData() {
        List<Object[]> results = plantingPlanRepository.countByStatus();
        return results.stream()
            .map(result -> {
                Map<String, Object> map = new HashMap<>();
                // result[0] 是 PlantingStatus 枚举，需要先转换为枚举类型
                PlantingPlan.PlantingStatus status = (PlantingPlan.PlantingStatus) result[0];
                map.put("name", getPlanStatusDisplayName(status.name()));
                map.put("value", result[1]);
                return map;
            })
            .collect(Collectors.toList());
    }

    /**
     * 获取作物产量趋势数据
     * 这里返回最近6个月的模拟数据，实际项目中应该从种植记录中统计
     */
    private DashboardResponse.ProductionTrendData getProductionTrendData() {
        // 这里返回最近6个月的数据
        List<String> months = Arrays.asList("1月", "2月", "3月", "4月", "5月", "6月");

        // 这里应该从种植记录中统计实际产量数据
        // 现在返回模拟数据
        List<DashboardResponse.CropProductionData> crops = Arrays.asList(
            DashboardResponse.CropProductionData.builder()
                .name("小麦")
                .data(Arrays.asList(120.0, 132.0, 101.0, 134.0, 90.0, 230.0))
                .build(),
            DashboardResponse.CropProductionData.builder()
                .name("玉米")
                .data(Arrays.asList(220.0, 182.0, 191.0, 234.0, 290.0, 330.0))
                .build(),
            DashboardResponse.CropProductionData.builder()
                .name("水稻")
                .data(Arrays.asList(150.0, 232.0, 201.0, 154.0, 190.0, 330.0))
                .build(),
            DashboardResponse.CropProductionData.builder()
                .name("番茄")
                .data(Arrays.asList(320.0, 332.0, 301.0, 334.0, 390.0, 330.0))
                .build()
        );

        return DashboardResponse.ProductionTrendData.builder()
            .months(months)
            .crops(crops)
            .build();
    }

    /**
     * 获取近期提醒
     */
    private List<DashboardResponse.NotificationItem> getRecentNotifications() {
        // 获取最近7天内需要发送的通知，按计划发送时间倒序排列
        LocalDateTime sevenDaysAgo = LocalDateTime.now().minusDays(7);
        LocalDateTime futureDate = LocalDateTime.now().plusDays(7);

        List<Notification> notifications = notificationRepository
            .findByScheduledTimeBetweenOrderByScheduledTimeDesc(sevenDaysAgo, futureDate);

        return notifications.stream()
            .limit(10) // 最多返回10条
            .map(this::convertToNotificationItem)
            .collect(Collectors.toList());
    }

    private DashboardResponse.NotificationItem convertToNotificationItem(Notification notification) {
        return DashboardResponse.NotificationItem.builder()
            .id(notification.getId())
            .title(notification.getTitle())
            .content(notification.getContent())
            .scheduledTime(notification.getScheduledTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
            .priority(notification.getPriority().name())
            .type(notification.getType().name())
            .build();
    }

    private String getFarmlandStatusDisplayName(String status) {
        switch (status) {
            case "AVAILABLE": return "可用";
            case "PLANTED": return "已种植";
            case "MAINTENANCE": return "维护中";
            default: return status;
        }
    }

    private String getCropCategoryDisplayName(String category) {
        switch (category) {
            case "VEGETABLES": return "蔬菜类";
            case "GRAINS": return "粮食类";
            case "ECONOMIC_CROPS": return "经济作物";
            case "OTHER": return "其它";
            default: return category;
        }
    }

    private String getPlanStatusDisplayName(String status) {
        switch (status) {
            case "PLANNED": return "计划中";
            case "IN_PROGRESS": return "进行中";
            case "COMPLETED": return "已完成";
            case "CANCELLED": return "已取消";
            default: return status;
        }
    }
}
