package com.agricultural.management.dto;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class DataAnalysisResponse {

    // 作物生长分析数据
    private CropGrowthAnalysisData cropGrowthAnalysis;

    // 农田利用率分析数据
    private FieldUtilizationAnalysisData fieldUtilizationAnalysis;

    // 综合统计数据
    private ComprehensiveStatisticsData comprehensiveStatistics;

    public DataAnalysisResponse() {
    }

    public DataAnalysisResponse(CropGrowthAnalysisData cropGrowthAnalysis,
                              FieldUtilizationAnalysisData fieldUtilizationAnalysis,
                              ComprehensiveStatisticsData comprehensiveStatistics) {
        this.cropGrowthAnalysis = cropGrowthAnalysis;
        this.fieldUtilizationAnalysis = fieldUtilizationAnalysis;
        this.comprehensiveStatistics = comprehensiveStatistics;
    }

    // Getters and Setters
    public CropGrowthAnalysisData getCropGrowthAnalysis() {
        return cropGrowthAnalysis;
    }

    public void setCropGrowthAnalysis(CropGrowthAnalysisData cropGrowthAnalysis) {
        this.cropGrowthAnalysis = cropGrowthAnalysis;
    }

    public FieldUtilizationAnalysisData getFieldUtilizationAnalysis() {
        return fieldUtilizationAnalysis;
    }

    public void setFieldUtilizationAnalysis(FieldUtilizationAnalysisData fieldUtilizationAnalysis) {
        this.fieldUtilizationAnalysis = fieldUtilizationAnalysis;
    }

    public ComprehensiveStatisticsData getComprehensiveStatistics() {
        return comprehensiveStatistics;
    }

    public void setComprehensiveStatistics(ComprehensiveStatisticsData comprehensiveStatistics) {
        this.comprehensiveStatistics = comprehensiveStatistics;
    }

    public static class CropGrowthAnalysisData {
        private List<CropGrowthTrend> growthTrends;
        private List<Map<String, Object>> healthStatusDistribution;
        private List<EnvironmentalData> environmentalData;

        public CropGrowthAnalysisData() {
        }

        public CropGrowthAnalysisData(List<CropGrowthTrend> growthTrends,
                                    List<Map<String, Object>> healthStatusDistribution,
                                    List<EnvironmentalData> environmentalData) {
            this.growthTrends = growthTrends;
            this.healthStatusDistribution = healthStatusDistribution;
            this.environmentalData = environmentalData;
        }

        public List<CropGrowthTrend> getGrowthTrends() {
            return growthTrends;
        }

        public void setGrowthTrends(List<CropGrowthTrend> growthTrends) {
            this.growthTrends = growthTrends;
        }

        public List<Map<String, Object>> getHealthStatusDistribution() {
            return healthStatusDistribution;
        }

        public void setHealthStatusDistribution(List<Map<String, Object>> healthStatusDistribution) {
            this.healthStatusDistribution = healthStatusDistribution;
        }

        public List<EnvironmentalData> getEnvironmentalData() {
            return environmentalData;
        }

        public void setEnvironmentalData(List<EnvironmentalData> environmentalData) {
            this.environmentalData = environmentalData;
        }
    }

    public static class CropGrowthTrend {
        private String cropName;
        private List<GrowthDataPoint> dataPoints;

        public CropGrowthTrend() {
        }

        public CropGrowthTrend(String cropName, List<GrowthDataPoint> dataPoints) {
            this.cropName = cropName;
            this.dataPoints = dataPoints;
        }

        public String getCropName() {
            return cropName;
        }

        public void setCropName(String cropName) {
            this.cropName = cropName;
        }

        public List<GrowthDataPoint> getDataPoints() {
            return dataPoints;
        }

        public void setDataPoints(List<GrowthDataPoint> dataPoints) {
            this.dataPoints = dataPoints;
        }
    }

    public static class GrowthDataPoint {
        private String date;
        private Double height;
        private Double width;
        private String healthStatus;

        public GrowthDataPoint() {
        }

        public GrowthDataPoint(String date, Double height, Double width, String healthStatus) {
            this.date = date;
            this.height = height;
            this.width = width;
            this.healthStatus = healthStatus;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Double getHeight() {
            return height;
        }

        public void setHeight(Double height) {
            this.height = height;
        }

        public Double getWidth() {
            return width;
        }

        public void setWidth(Double width) {
            this.width = width;
        }

        public String getHealthStatus() {
            return healthStatus;
        }

        public void setHealthStatus(String healthStatus) {
            this.healthStatus = healthStatus;
        }
    }

    public static class EnvironmentalData {
        private String date;
        private Double temperature;
        private Double humidity;
        private Double soilMoisture;
        private Double lightIntensity;

        public EnvironmentalData() {
        }

        public EnvironmentalData(String date, Double temperature, Double humidity,
                               Double soilMoisture, Double lightIntensity) {
            this.date = date;
            this.temperature = temperature;
            this.humidity = humidity;
            this.soilMoisture = soilMoisture;
            this.lightIntensity = lightIntensity;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Double getTemperature() {
            return temperature;
        }

        public void setTemperature(Double temperature) {
            this.temperature = temperature;
        }

        public Double getHumidity() {
            return humidity;
        }

        public void setHumidity(Double humidity) {
            this.humidity = humidity;
        }

        public Double getSoilMoisture() {
            return soilMoisture;
        }

        public void setSoilMoisture(Double soilMoisture) {
            this.soilMoisture = soilMoisture;
        }

        public Double getLightIntensity() {
            return lightIntensity;
        }

        public void setLightIntensity(Double lightIntensity) {
            this.lightIntensity = lightIntensity;
        }
    }

    public static class FieldUtilizationAnalysisData {
        private List<Map<String, Object>> fieldStatusDistribution;
        private List<FieldUtilizationTrend> utilizationTrends;
        private List<FieldUsageDetail> fieldUsageDetails;

        public FieldUtilizationAnalysisData() {
        }

        public FieldUtilizationAnalysisData(List<Map<String, Object>> fieldStatusDistribution,
                                          List<FieldUtilizationTrend> utilizationTrends,
                                          List<FieldUsageDetail> fieldUsageDetails) {
            this.fieldStatusDistribution = fieldStatusDistribution;
            this.utilizationTrends = utilizationTrends;
            this.fieldUsageDetails = fieldUsageDetails;
        }

        public List<Map<String, Object>> getFieldStatusDistribution() {
            return fieldStatusDistribution;
        }

        public void setFieldStatusDistribution(List<Map<String, Object>> fieldStatusDistribution) {
            this.fieldStatusDistribution = fieldStatusDistribution;
        }

        public List<FieldUtilizationTrend> getUtilizationTrends() {
            return utilizationTrends;
        }

        public void setUtilizationTrends(List<FieldUtilizationTrend> utilizationTrends) {
            this.utilizationTrends = utilizationTrends;
        }

        public List<FieldUsageDetail> getFieldUsageDetails() {
            return fieldUsageDetails;
        }

        public void setFieldUsageDetails(List<FieldUsageDetail> fieldUsageDetails) {
            this.fieldUsageDetails = fieldUsageDetails;
        }
    }

    public static class FieldUtilizationTrend {
        private String month;
        private Double utilizationRate;

        public FieldUtilizationTrend() {
        }

        public FieldUtilizationTrend(String month, Double utilizationRate) {
            this.month = month;
            this.utilizationRate = utilizationRate;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public Double getUtilizationRate() {
            return utilizationRate;
        }

        public void setUtilizationRate(Double utilizationRate) {
            this.utilizationRate = utilizationRate;
        }
    }

    public static class FieldUsageDetail {
        private String fieldName;
        private Double area;
        private String status;
        private String cropName;
        private Double utilizationRate;
        private String lastActivity;

        public FieldUsageDetail() {
        }

        public FieldUsageDetail(String fieldName, Double area, String status,
                              String cropName, Double utilizationRate, String lastActivity) {
            this.fieldName = fieldName;
            this.area = area;
            this.status = status;
            this.cropName = cropName;
            this.utilizationRate = utilizationRate;
            this.lastActivity = lastActivity;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public Double getArea() {
            return area;
        }

        public void setArea(Double area) {
            this.area = area;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCropName() {
            return cropName;
        }

        public void setCropName(String cropName) {
            this.cropName = cropName;
        }

        public Double getUtilizationRate() {
            return utilizationRate;
        }

        public void setUtilizationRate(Double utilizationRate) {
            this.utilizationRate = utilizationRate;
        }

        public String getLastActivity() {
            return lastActivity;
        }

        public void setLastActivity(String lastActivity) {
            this.lastActivity = lastActivity;
        }
    }

    public static class ComprehensiveStatisticsData {
        private StatisticsOverview overview;
        private List<ActivityTrend> activityTrends;
        private List<Map<String, Object>> moduleUsage;
        private List<ModuleStatistics> moduleStatistics;

        public ComprehensiveStatisticsData() {
        }

        public ComprehensiveStatisticsData(StatisticsOverview overview,
                                         List<ActivityTrend> activityTrends,
                                         List<Map<String, Object>> moduleUsage,
                                         List<ModuleStatistics> moduleStatistics) {
            this.overview = overview;
            this.activityTrends = activityTrends;
            this.moduleUsage = moduleUsage;
            this.moduleStatistics = moduleStatistics;
        }

        public StatisticsOverview getOverview() {
            return overview;
        }

        public void setOverview(StatisticsOverview overview) {
            this.overview = overview;
        }

        public List<ActivityTrend> getActivityTrends() {
            return activityTrends;
        }

        public void setActivityTrends(List<ActivityTrend> activityTrends) {
            this.activityTrends = activityTrends;
        }

        public List<Map<String, Object>> getModuleUsage() {
            return moduleUsage;
        }

        public void setModuleUsage(List<Map<String, Object>> moduleUsage) {
            this.moduleUsage = moduleUsage;
        }

        public List<ModuleStatistics> getModuleStatistics() {
            return moduleStatistics;
        }

        public void setModuleStatistics(List<ModuleStatistics> moduleStatistics) {
            this.moduleStatistics = moduleStatistics;
        }
    }

    public static class StatisticsOverview {
        private Long userCount;
        private Long cropCount;
        private Long farmlandCount;
        private Long planCount;
        private Long monitoringCount;
        private Long reportCount;

        public StatisticsOverview() {
        }

        public StatisticsOverview(Long userCount, Long cropCount, Long farmlandCount,
                                Long planCount, Long monitoringCount, Long reportCount) {
            this.userCount = userCount;
            this.cropCount = cropCount;
            this.farmlandCount = farmlandCount;
            this.planCount = planCount;
            this.monitoringCount = monitoringCount;
            this.reportCount = reportCount;
        }

        public Long getUserCount() {
            return userCount;
        }

        public void setUserCount(Long userCount) {
            this.userCount = userCount;
        }

        public Long getCropCount() {
            return cropCount;
        }

        public void setCropCount(Long cropCount) {
            this.cropCount = cropCount;
        }

        public Long getFarmlandCount() {
            return farmlandCount;
        }

        public void setFarmlandCount(Long farmlandCount) {
            this.farmlandCount = farmlandCount;
        }

        public Long getPlanCount() {
            return planCount;
        }

        public void setPlanCount(Long planCount) {
            this.planCount = planCount;
        }

        public Long getMonitoringCount() {
            return monitoringCount;
        }

        public void setMonitoringCount(Long monitoringCount) {
            this.monitoringCount = monitoringCount;
        }

        public Long getReportCount() {
            return reportCount;
        }

        public void setReportCount(Long reportCount) {
            this.reportCount = reportCount;
        }
    }

    public static class ActivityTrend {
        private String period;
        private Long newRecords;
        private Long activeUsers;

        public ActivityTrend() {
        }

        public ActivityTrend(String period, Long newRecords, Long activeUsers) {
            this.period = period;
            this.newRecords = newRecords;
            this.activeUsers = activeUsers;
        }

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }

        public Long getNewRecords() {
            return newRecords;
        }

        public void setNewRecords(Long newRecords) {
            this.newRecords = newRecords;
        }

        public Long getActiveUsers() {
            return activeUsers;
        }

        public void setActiveUsers(Long activeUsers) {
            this.activeUsers = activeUsers;
        }
    }

    public static class ModuleStatistics {
        private String moduleName;
        private Long totalRecords;
        private Long activeRecords;
        private Double growthRate;
        private String lastUpdate;

        public ModuleStatistics() {
        }

        public ModuleStatistics(String moduleName, Long totalRecords, Long activeRecords,
                              Double growthRate, String lastUpdate) {
            this.moduleName = moduleName;
            this.totalRecords = totalRecords;
            this.activeRecords = activeRecords;
            this.growthRate = growthRate;
            this.lastUpdate = lastUpdate;
        }

        public String getModuleName() {
            return moduleName;
        }

        public void setModuleName(String moduleName) {
            this.moduleName = moduleName;
        }

        public Long getTotalRecords() {
            return totalRecords;
        }

        public void setTotalRecords(Long totalRecords) {
            this.totalRecords = totalRecords;
        }

        public Long getActiveRecords() {
            return activeRecords;
        }

        public void setActiveRecords(Long activeRecords) {
            this.activeRecords = activeRecords;
        }

        public Double getGrowthRate() {
            return growthRate;
        }

        public void setGrowthRate(Double growthRate) {
            this.growthRate = growthRate;
        }

        public String getLastUpdate() {
            return lastUpdate;
        }

        public void setLastUpdate(String lastUpdate) {
            this.lastUpdate = lastUpdate;
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private CropGrowthAnalysisData cropGrowthAnalysis;
        private FieldUtilizationAnalysisData fieldUtilizationAnalysis;
        private ComprehensiveStatisticsData comprehensiveStatistics;

        public Builder cropGrowthAnalysis(CropGrowthAnalysisData cropGrowthAnalysis) {
            this.cropGrowthAnalysis = cropGrowthAnalysis;
            return this;
        }

        public Builder fieldUtilizationAnalysis(FieldUtilizationAnalysisData fieldUtilizationAnalysis) {
            this.fieldUtilizationAnalysis = fieldUtilizationAnalysis;
            return this;
        }

        public Builder comprehensiveStatistics(ComprehensiveStatisticsData comprehensiveStatistics) {
            this.comprehensiveStatistics = comprehensiveStatistics;
            return this;
        }

        public DataAnalysisResponse build() {
            return new DataAnalysisResponse(cropGrowthAnalysis, fieldUtilizationAnalysis, comprehensiveStatistics);
        }
    }
}
