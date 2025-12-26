package com.agricultural.management.service;

import com.agricultural.management.dto.*;
import com.agricultural.management.entity.Crop;
import com.agricultural.management.entity.GrowthMonitoring;
import com.agricultural.management.entity.PlantingPlan;
import com.agricultural.management.entity.User;
import com.agricultural.management.exception.ResourceNotFoundException;
import com.agricultural.management.repository.CropRepository;
import com.agricultural.management.repository.GrowthMonitoringRepository;
import com.agricultural.management.repository.PlantingPlanRepository;
import com.agricultural.management.repository.UserRepository;
import com.agricultural.management.security.UserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GrowthMonitoringService {

    private final GrowthMonitoringRepository growthMonitoringRepository;
    private final PlantingPlanRepository plantingPlanRepository;
    private final CropRepository cropRepository;
    private final UserRepository userRepository;

    public GrowthMonitoringService(GrowthMonitoringRepository growthMonitoringRepository,
                                 PlantingPlanRepository plantingPlanRepository,
                                 CropRepository cropRepository,
                                 UserRepository userRepository) {
        this.growthMonitoringRepository = growthMonitoringRepository;
        this.plantingPlanRepository = plantingPlanRepository;
        this.cropRepository = cropRepository;
        this.userRepository = userRepository;
    }

    /**
     * 分页查询生长监测列表
     */
    public PageResponse<GrowthMonitoringResponse> getGrowthMonitoringList(GrowthMonitoringQueryRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());

        Specification<GrowthMonitoring> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getPlanId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("planId"), request.getPlanId()));
            }

            if (request.getMonitoringDateFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("monitoringDate"), request.getMonitoringDateFrom()));
            }

            if (request.getMonitoringDateTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("monitoringDate"), request.getMonitoringDateTo()));
            }

            if (request.getHealthStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("healthStatus"), request.getHealthStatus()));
            }

            if (request.getCreatedBy() != null) {
                predicates.add(criteriaBuilder.equal(root.get("createdBy"), request.getCreatedBy()));
            }

            // 数值范围查询
            if (request.getMinHeight() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("heightMeasurement"), request.getMinHeight()));
            }

            if (request.getMaxHeight() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("heightMeasurement"), request.getMaxHeight()));
            }

            if (request.getMinWidth() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("widthMeasurement"), request.getMinWidth()));
            }

            if (request.getMaxWidth() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("widthMeasurement"), request.getMaxWidth()));
            }

            if (request.getMinTemperature() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("temperature"), request.getMinTemperature()));
            }

            if (request.getMaxTemperature() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("temperature"), request.getMaxTemperature()));
            }

            if (request.getMinHumidity() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("humidity"), request.getMinHumidity()));
            }

            if (request.getMaxHumidity() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("humidity"), request.getMaxHumidity()));
            }

            if (request.getMinSoilMoisture() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("soilMoisture"), request.getMinSoilMoisture()));
            }

            if (request.getMaxSoilMoisture() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("soilMoisture"), request.getMaxSoilMoisture()));
            }

            if (request.getMinPhLevel() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("phLevel"), request.getMinPhLevel()));
            }

            if (request.getMaxPhLevel() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("phLevel"), request.getMaxPhLevel()));
            }

            // 添加关键词搜索功能 - 搜索备注
            if (request.getKeyword() != null && !request.getKeyword().trim().isEmpty()) {
                String keyword = "%" + request.getKeyword().trim().toLowerCase() + "%";
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("notes")), keyword));
            }

            if (predicates.isEmpty()) {
                return null; // 没有查询条件，返回所有数据
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<GrowthMonitoring> growthMonitoringPage = growthMonitoringRepository.findAll(spec, pageable);

        List<GrowthMonitoringResponse> growthMonitoringResponses = growthMonitoringPage.getContent().stream()
            .map(this::convertToGrowthMonitoringResponse)
            .collect(Collectors.toList());

        return PageResponse.<GrowthMonitoringResponse>builder()
            .content(growthMonitoringResponses)
            .page(growthMonitoringPage.getNumber() + 1)
            .size(growthMonitoringPage.getSize())
            .totalElements(growthMonitoringPage.getTotalElements())
            .totalPages(growthMonitoringPage.getTotalPages())
            .last(growthMonitoringPage.isLast())
            .build();
    }

    /**
     * 根据ID获取生长监测信息
     */
    public GrowthMonitoringResponse getGrowthMonitoringById(Long id) {
        GrowthMonitoring growthMonitoring = growthMonitoringRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("生长监测记录不存在，ID: " + id));
        return convertToGrowthMonitoringResponse(growthMonitoring);
    }

    /**
     * 创建新生长监测记录
     */
    @Transactional
    public GrowthMonitoringResponse createGrowthMonitoring(GrowthMonitoringCreateRequest request) {
        // 验证种植计划是否存在
        PlantingPlan plantingPlan = plantingPlanRepository.findById(request.getPlanId())
            .orElseThrow(() -> new ResourceNotFoundException("种植计划不存在，ID: " + request.getPlanId()));

        // 验证监测日期是否在种植计划的时间范围内
        if (request.getMonitoringDate().isBefore(plantingPlan.getPlannedStartDate()) ||
            request.getMonitoringDate().isAfter(plantingPlan.getPlannedEndDate())) {
            throw new IllegalArgumentException("监测日期必须在种植计划的时间范围内");
        }

        // 检查是否已经存在同一天的监测记录（同一个种植计划每天只能有一条监测记录）
        boolean exists = growthMonitoringRepository.existsByPlanIdAndMonitoringDate(
            request.getPlanId(), request.getMonitoringDate());

        if (exists) {
            throw new IllegalArgumentException("该种植计划在指定日期已存在监测记录");
        }

        // 获取当前用户ID
        Long currentUserId = getCurrentUserId();

        GrowthMonitoring growthMonitoring = new GrowthMonitoring();
        growthMonitoring.setPlanId(request.getPlanId());
        growthMonitoring.setMonitoringDate(request.getMonitoringDate());
        growthMonitoring.setHeightMeasurement(request.getHeightMeasurement());
        growthMonitoring.setWidthMeasurement(request.getWidthMeasurement());
        growthMonitoring.setHealthStatus(request.getHealthStatus());
        growthMonitoring.setSoilMoisture(request.getSoilMoisture());
        growthMonitoring.setTemperature(request.getTemperature());
        growthMonitoring.setHumidity(request.getHumidity());
        growthMonitoring.setLightIntensity(request.getLightIntensity());
        growthMonitoring.setPhLevel(request.getPhLevel());
        growthMonitoring.setNotes(request.getNotes());
        growthMonitoring.setPhotoUrl(request.getPhotoUrl());
        growthMonitoring.setCreatedBy(currentUserId);

        GrowthMonitoring savedGrowthMonitoring = growthMonitoringRepository.save(growthMonitoring);
        return convertToGrowthMonitoringResponse(savedGrowthMonitoring);
    }

    /**
     * 更新生长监测信息
     */
    @Transactional
    public GrowthMonitoringResponse updateGrowthMonitoring(Long id, GrowthMonitoringUpdateRequest request) {
        GrowthMonitoring growthMonitoring = growthMonitoringRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("生长监测记录不存在，ID: " + id));

        // 检查权限（只有创建者可以修改记录）
        Long currentUserId = getCurrentUserId();
        if (!growthMonitoring.getCreatedBy().equals(currentUserId)) {
            throw new RuntimeException("无权修改此生长监测记录");
        }

        // 如果更新种植计划ID，验证种植计划存在性
        if (request.getPlanId() != null && !request.getPlanId().equals(growthMonitoring.getPlanId())) {
            PlantingPlan plantingPlan = plantingPlanRepository.findById(request.getPlanId())
                .orElseThrow(() -> new ResourceNotFoundException("种植计划不存在，ID: " + request.getPlanId()));

            // 验证监测日期是否在新的种植计划时间范围内
            LocalDate monitoringDate = request.getMonitoringDate() != null ? request.getMonitoringDate() : growthMonitoring.getMonitoringDate();
            if (monitoringDate.isBefore(plantingPlan.getPlannedStartDate()) ||
                monitoringDate.isAfter(plantingPlan.getPlannedEndDate())) {
                throw new IllegalArgumentException("监测日期必须在种植计划的时间范围内");
            }
        }

        // 检查唯一性约束（排除当前记录）
        LocalDate monitoringDate = request.getMonitoringDate() != null ? request.getMonitoringDate() : growthMonitoring.getMonitoringDate();
        Long planId = request.getPlanId() != null ? request.getPlanId() : growthMonitoring.getPlanId();

        if (!monitoringDate.equals(growthMonitoring.getMonitoringDate()) ||
            !planId.equals(growthMonitoring.getPlanId())) {

            boolean exists = growthMonitoringRepository.existsByPlanIdAndMonitoringDate(planId, monitoringDate);

            if (exists) {
                throw new IllegalArgumentException("该种植计划在指定日期已存在监测记录");
            }
        }

        // 更新字段
        if (request.getPlanId() != null) {
            growthMonitoring.setPlanId(request.getPlanId());
        }
        if (request.getMonitoringDate() != null) {
            growthMonitoring.setMonitoringDate(request.getMonitoringDate());
        }
        if (request.getHeightMeasurement() != null) {
            growthMonitoring.setHeightMeasurement(request.getHeightMeasurement());
        }
        if (request.getWidthMeasurement() != null) {
            growthMonitoring.setWidthMeasurement(request.getWidthMeasurement());
        }
        if (request.getHealthStatus() != null) {
            growthMonitoring.setHealthStatus(request.getHealthStatus());
        }
        if (request.getSoilMoisture() != null) {
            growthMonitoring.setSoilMoisture(request.getSoilMoisture());
        }
        if (request.getTemperature() != null) {
            growthMonitoring.setTemperature(request.getTemperature());
        }
        if (request.getHumidity() != null) {
            growthMonitoring.setHumidity(request.getHumidity());
        }
        if (request.getLightIntensity() != null) {
            growthMonitoring.setLightIntensity(request.getLightIntensity());
        }
        if (request.getPhLevel() != null) {
            growthMonitoring.setPhLevel(request.getPhLevel());
        }
        if (request.getNotes() != null) {
            growthMonitoring.setNotes(request.getNotes());
        }
        if (request.getPhotoUrl() != null) {
            growthMonitoring.setPhotoUrl(request.getPhotoUrl());
        }

        GrowthMonitoring updatedGrowthMonitoring = growthMonitoringRepository.save(growthMonitoring);
        return convertToGrowthMonitoringResponse(updatedGrowthMonitoring);
    }

    /**
     * 删除生长监测记录
     */
    @Transactional
    public void deleteGrowthMonitoring(Long id) {
        GrowthMonitoring growthMonitoring = growthMonitoringRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("生长监测记录不存在，ID: " + id));

        // 检查权限（只有创建者可以删除记录）
        Long currentUserId = getCurrentUserId();
        if (!growthMonitoring.getCreatedBy().equals(currentUserId)) {
            throw new RuntimeException("无权删除此生长监测记录");
        }

        growthMonitoringRepository.delete(growthMonitoring);
    }

    /**
     * 获取指定种植计划的监测记录列表
     */
    public List<GrowthMonitoringResponse> getGrowthMonitoringByPlan(Long planId) {
        // 验证种植计划存在
        plantingPlanRepository.findById(planId)
            .orElseThrow(() -> new ResourceNotFoundException("种植计划不存在，ID: " + planId));

        List<GrowthMonitoring> growthMonitorings = growthMonitoringRepository.findByPlanIdOrderByMonitoringDate(planId);
        return growthMonitorings.stream()
            .map(this::convertToGrowthMonitoringResponse)
            .collect(Collectors.toList());
    }

    /**
     * 获取指定种植计划在时间范围内的监测记录
     */
    public List<GrowthMonitoringResponse> getGrowthMonitoringByPlanAndDateRange(Long planId, LocalDate startDate, LocalDate endDate) {
        // 验证种植计划存在
        plantingPlanRepository.findById(planId)
            .orElseThrow(() -> new ResourceNotFoundException("种植计划不存在，ID: " + planId));

        List<GrowthMonitoring> growthMonitorings = growthMonitoringRepository.findByPlanIdAndMonitoringDateBetween(planId, startDate, endDate);
        return growthMonitorings.stream()
            .map(this::convertToGrowthMonitoringResponse)
            .collect(Collectors.toList());
    }

    /**
     * 获取指定种植计划和健康状态的监测记录
     */
    public List<GrowthMonitoringResponse> getGrowthMonitoringByPlanAndHealthStatus(Long planId, GrowthMonitoring.HealthStatus healthStatus) {
        // 验证种植计划存在
        plantingPlanRepository.findById(planId)
            .orElseThrow(() -> new ResourceNotFoundException("种植计划不存在，ID: " + planId));

        List<GrowthMonitoring> growthMonitorings = growthMonitoringRepository.findByPlanIdAndHealthStatus(planId, healthStatus);
        return growthMonitorings.stream()
            .map(this::convertToGrowthMonitoringResponse)
            .collect(Collectors.toList());
    }

    /**
     * 获取指定健康状态的监测记录列表
     */
    public List<GrowthMonitoringResponse> getGrowthMonitoringByHealthStatus(GrowthMonitoring.HealthStatus healthStatus) {
        List<GrowthMonitoring> growthMonitorings = growthMonitoringRepository.findByHealthStatus(healthStatus);
        return growthMonitorings.stream()
            .map(this::convertToGrowthMonitoringResponse)
            .collect(Collectors.toList());
    }

    /**
     * 获取当前用户的监测记录列表
     */
    public List<GrowthMonitoringResponse> getMyGrowthMonitoring() {
        Long currentUserId = getCurrentUserId();
        List<GrowthMonitoring> growthMonitorings = growthMonitoringRepository.findByCreatedBy(currentUserId);
        return growthMonitorings.stream()
            .map(this::convertToGrowthMonitoringResponse)
            .collect(Collectors.toList());
    }

    /**
     * 获取指定日期范围内的监测记录
     */
    public List<GrowthMonitoringResponse> getGrowthMonitoringByDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("开始日期不能晚于结束日期");
        }

        List<GrowthMonitoring> growthMonitorings = growthMonitoringRepository.findByMonitoringDateBetween(startDate, endDate);
        return growthMonitorings.stream()
            .map(this::convertToGrowthMonitoringResponse)
            .collect(Collectors.toList());
    }

    /**
     * 获取温度范围内的监测记录
     */
    public List<GrowthMonitoringResponse> getGrowthMonitoringByTemperatureRange(BigDecimal minTemp, BigDecimal maxTemp) {
        List<GrowthMonitoring> growthMonitorings = growthMonitoringRepository.findByTemperatureBetween(minTemp, maxTemp);
        return growthMonitorings.stream()
            .map(this::convertToGrowthMonitoringResponse)
            .collect(Collectors.toList());
    }

    /**
     * 获取土壤湿度范围内的监测记录
     */
    public List<GrowthMonitoringResponse> getGrowthMonitoringBySoilMoistureRange(BigDecimal minMoisture, BigDecimal maxMoisture) {
        List<GrowthMonitoring> growthMonitorings = growthMonitoringRepository.findBySoilMoistureBetween(minMoisture, maxMoisture);
        return growthMonitorings.stream()
            .map(this::convertToGrowthMonitoringResponse)
            .collect(Collectors.toList());
    }

    /**
     * 获取pH值范围内的监测记录
     */
    public List<GrowthMonitoringResponse> getGrowthMonitoringByPhRange(BigDecimal minPh, BigDecimal maxPh) {
        List<GrowthMonitoring> growthMonitorings = growthMonitoringRepository.findByPhLevelBetween(minPh, maxPh);
        return growthMonitorings.stream()
            .map(this::convertToGrowthMonitoringResponse)
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

    /**
     * 采集生长监测数据 - 为所有种植计划生成当日的监测记录
     */
    @Transactional
    public int collectGrowthMonitoringData() {
        LocalDate today = LocalDate.now();
        List<PlantingPlan> allPlans = plantingPlanRepository.findAll();
        int collectedCount = 0;

        for (PlantingPlan plan : allPlans) {
            // 检查今天是否已经采集过这个计划的数据
            boolean alreadyCollected = growthMonitoringRepository.existsByPlanIdAndMonitoringDate(plan.getId(), today);
            if (alreadyCollected) {
                continue; // 跳过已采集的计划
            }

            // 获取作物信息用于生成合理的数据
            Crop crop = cropRepository.findById(plan.getCropId()).orElse(null);
            if (crop == null) {
                continue; // 如果找不到作物信息，跳过
            }

            // 生成基于作物特性的监测数据
            GrowthMonitoring monitoring = generateMonitoringData(plan, crop, today);
            growthMonitoringRepository.save(monitoring);
            collectedCount++;
        }

        return collectedCount;
    }

    /**
     * 根据种植计划和作物信息生成合理的监测数据
     */
    private GrowthMonitoring generateMonitoringData(PlantingPlan plan, Crop crop, LocalDate monitoringDate) {
        GrowthMonitoring monitoring = new GrowthMonitoring();
        monitoring.setPlanId(plan.getId());
        monitoring.setMonitoringDate(monitoringDate);

        // 根据作物类型和生长周期生成合理的生长数据
        long daysSinceStart = java.time.temporal.ChronoUnit.DAYS.between(plan.getPlannedStartDate(), monitoringDate);
        int growthDays = crop.getGrowthPeriod() != null ? crop.getGrowthPeriod() : 90;

        // 生成高度数据（基于生长天数和作物类型）
        double baseHeight = generateBaseHeight(crop.getName());
        double growthProgress = Math.min(daysSinceStart / (double) growthDays, 1.0);
        double height = baseHeight * growthProgress * (0.8 + Math.random() * 0.4); // 添加随机波动
        monitoring.setHeightMeasurement(BigDecimal.valueOf(Math.round(height * 100.0) / 100.0));

        // 生成宽度数据
        double width = height * (0.3 + Math.random() * 0.4); // 宽度通常是高度的30%-70%
        monitoring.setWidthMeasurement(BigDecimal.valueOf(Math.round(width * 100.0) / 100.0));

        // 生成健康状态（基于随机概率，主要为良好状态）
        double random = Math.random();
        GrowthMonitoring.HealthStatus healthStatus;
        if (random < 0.05) {
            healthStatus = GrowthMonitoring.HealthStatus.POOR;
        } else if (random < 0.25) {
            healthStatus = GrowthMonitoring.HealthStatus.FAIR;
        } else if (random < 0.60) {
            healthStatus = GrowthMonitoring.HealthStatus.GOOD;
        } else {
            healthStatus = GrowthMonitoring.HealthStatus.EXCELLENT;
        }
        monitoring.setHealthStatus(healthStatus);

        // 生成环境参数（模拟真实环境数据）
        generateEnvironmentalData(monitoring, monitoringDate);

        // 生成备注
        monitoring.setNotes("自动采集数据 - " + crop.getName() + "生长监测");

        // 设置创建者（获取当前用户）
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserPrincipal) {
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            monitoring.setCreatedBy(userPrincipal.getId());
        }

        return monitoring;
    }

    /**
     * 根据作物名称生成基础高度
     */
    private double generateBaseHeight(String cropName) {
        if (cropName == null) return 50.0;

        switch (cropName.toLowerCase()) {
            case "番茄":
                return 150.0; // 番茄最终高度约1.5米
            case "黄瓜":
                return 200.0; // 黄瓜最终高度约2米
            case "水稻":
                return 120.0; // 水稻最终高度约1.2米
            case "小麦":
                return 100.0; // 小麦最终高度约1米
            case "玉米":
                return 250.0; // 玉米最终高度约2.5米
            default:
                return 100.0; // 默认高度
        }
    }

    /**
     * 生成环境监测数据
     */
    private void generateEnvironmentalData(GrowthMonitoring monitoring, LocalDate date) {
        // 温度：根据月份生成合理温度
        int month = date.getMonthValue();
        double baseTemp = 15.0;
        if (month >= 6 && month <= 8) {
            baseTemp = 25.0; // 夏季
        } else if (month >= 9 && month <= 11) {
            baseTemp = 20.0; // 秋季
        } else if (month >= 12 || month <= 2) {
            baseTemp = 5.0;  // 冬季
        } else {
            baseTemp = 15.0; // 春季
        }
        double temperature = baseTemp + (Math.random() - 0.5) * 10; // 添加±5度的波动
        monitoring.setTemperature(BigDecimal.valueOf(Math.round(temperature * 100.0) / 100.0));

        // 湿度：50-90%之间
        double humidity = 50.0 + Math.random() * 40.0;
        monitoring.setHumidity(BigDecimal.valueOf(Math.round(humidity * 100.0) / 100.0));

        // 土壤湿度：40-80%之间
        double soilMoisture = 40.0 + Math.random() * 40.0;
        monitoring.setSoilMoisture(BigDecimal.valueOf(Math.round(soilMoisture * 100.0) / 100.0));

        // 光照强度：根据时间生成合理值
        double lightIntensity = 30000 + Math.random() * 70000; // 30,000-100,000 lux
        monitoring.setLightIntensity((int) lightIntensity);

        // pH值：5.5-7.5之间
        double phLevel = 5.5 + Math.random() * 2.0;
        monitoring.setPhLevel(BigDecimal.valueOf(Math.round(phLevel * 100.0) / 100.0));
    }

    private GrowthMonitoringResponse convertToGrowthMonitoringResponse(GrowthMonitoring growthMonitoring) {
        return new GrowthMonitoringResponse(
            growthMonitoring.getId(),
            growthMonitoring.getPlanId(),
            growthMonitoring.getMonitoringDate(),
            growthMonitoring.getHeightMeasurement(),
            growthMonitoring.getWidthMeasurement(),
            growthMonitoring.getHealthStatus(),
            growthMonitoring.getSoilMoisture(),
            growthMonitoring.getTemperature(),
            growthMonitoring.getHumidity(),
            growthMonitoring.getLightIntensity(),
            growthMonitoring.getPhLevel(),
            growthMonitoring.getNotes(),
            growthMonitoring.getPhotoUrl(),
            growthMonitoring.getCreatedBy(),
            growthMonitoring.getCreatedAt(),
            growthMonitoring.getUpdatedAt()
        );
    }
}
