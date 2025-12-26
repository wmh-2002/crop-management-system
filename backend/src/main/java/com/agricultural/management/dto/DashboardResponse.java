package com.agricultural.management.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    // 基础统计数据
    private Long userCount;
    private Long cropCount;
    private Long farmlandCount;
    private Long planCount;

    // 图表数据
    private ProductionTrendData productionTrendData;
    private List<Map<String, Object>> farmlandStatusData;
    private List<Map<String, Object>> cropCategoryData;
    private List<Map<String, Object>> planStatusData;

    // 近期提醒
    private List<NotificationItem> recentNotifications;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProductionTrendData {
        private List<String> months;
        private List<CropProductionData> crops;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CropProductionData {
        private String name;
        private List<Double> data;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationItem {
        private Long id;
        private String title;
        private String content;
        private String scheduledTime;
        private String priority;
        private String type;
    }
}
