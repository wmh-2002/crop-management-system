package com.agricultural.management.service;

import com.agricultural.management.dto.*;
import com.agricultural.management.entity.GrowthMonitoring;
import com.agricultural.management.entity.PlantingPlan;
import com.agricultural.management.entity.User;
import com.agricultural.management.exception.ResourceNotFoundException;
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
    private final UserRepository userRepository;

    public GrowthMonitoringService(GrowthMonitoringRepository growthMonitoringRepository,
                                 PlantingPlanRepository plantingPlanRepository,
                                 UserRepository userRepository) {
        this.growthMonitoringRepository = growthMonitoringRepository;
        this.plantingPlanRepository = plantingPlanRepository;
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
