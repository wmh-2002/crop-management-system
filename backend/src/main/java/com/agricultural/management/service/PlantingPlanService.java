package com.agricultural.management.service;

import com.agricultural.management.dto.*;
import com.agricultural.management.entity.Farmland;
import com.agricultural.management.entity.Crop;
import com.agricultural.management.entity.PlantingPlan;
import com.agricultural.management.entity.User;
import com.agricultural.management.exception.ResourceNotFoundException;
import com.agricultural.management.repository.FarmlandRepository;
import com.agricultural.management.repository.CropRepository;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlantingPlanService {

    private final PlantingPlanRepository plantingPlanRepository;
    private final FarmlandRepository farmlandRepository;
    private final CropRepository cropRepository;
    private final UserRepository userRepository;

    public PlantingPlanService(PlantingPlanRepository plantingPlanRepository,
                             FarmlandRepository farmlandRepository,
                             CropRepository cropRepository,
                             UserRepository userRepository) {
        this.plantingPlanRepository = plantingPlanRepository;
        this.farmlandRepository = farmlandRepository;
        this.cropRepository = cropRepository;
        this.userRepository = userRepository;
    }

    /**
     * 分页查询种植计划列表
     */
    public PageResponse<PlantingPlanResponse> getPlantingPlanList(PlantingPlanQueryRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());

        Specification<PlantingPlan> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getPlanName() != null && !request.getPlanName().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("planName"),
                    "%" + request.getPlanName().trim() + "%"));
            }

            if (request.getFarmlandId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("farmlandId"), request.getFarmlandId()));
            }

            if (request.getCropId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("cropId"), request.getCropId()));
            }

            if (request.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), request.getStatus()));
            }

            if (request.getCreatedBy() != null) {
                predicates.add(criteriaBuilder.equal(root.get("createdBy"), request.getCreatedBy()));
            }

            if (request.getStartDateFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("plannedStartDate"), request.getStartDateFrom()));
            }

            if (request.getStartDateTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("plannedStartDate"), request.getStartDateTo()));
            }

            if (request.getEndDateFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("plannedEndDate"), request.getEndDateFrom()));
            }

            if (request.getEndDateTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("plannedEndDate"), request.getEndDateTo()));
            }

            if (predicates.isEmpty()) {
                return null; // 没有查询条件，返回所有数据
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<PlantingPlan> plantingPlanPage = plantingPlanRepository.findAll(spec, pageable);

        List<PlantingPlanResponse> plantingPlanResponses = plantingPlanPage.getContent().stream()
            .map(this::convertToPlantingPlanResponse)
            .collect(Collectors.toList());

        return PageResponse.<PlantingPlanResponse>builder()
            .content(plantingPlanResponses)
            .page(plantingPlanPage.getNumber() + 1)
            .size(plantingPlanPage.getSize())
            .totalElements(plantingPlanPage.getTotalElements())
            .totalPages(plantingPlanPage.getTotalPages())
            .last(plantingPlanPage.isLast())
            .build();
    }

    /**
     * 根据ID获取种植计划信息
     */
    public PlantingPlanResponse getPlantingPlanById(Long id) {
        PlantingPlan plantingPlan = plantingPlanRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("种植计划不存在，ID: " + id));
        return convertToPlantingPlanResponse(plantingPlan);
    }

    /**
     * 创建新种植计划
     */
    @Transactional
    public PlantingPlanResponse createPlantingPlan(PlantingPlanCreateRequest request) {
        // 验证农田是否存在
        Farmland farmland = farmlandRepository.findById(request.getFarmlandId())
            .orElseThrow(() -> new ResourceNotFoundException("农田不存在，ID: " + request.getFarmlandId()));

        // 验证作物是否存在
        Crop crop = cropRepository.findById(request.getCropId())
            .orElseThrow(() -> new ResourceNotFoundException("作物不存在，ID: " + request.getCropId()));

        // 验证日期逻辑
        if (request.getPlannedStartDate().isAfter(request.getPlannedEndDate())) {
            throw new IllegalArgumentException("计划开始日期不能晚于计划结束日期");
        }

        if (request.getExpectedHarvestDate() != null &&
            request.getExpectedHarvestDate().isBefore(request.getPlannedEndDate())) {
            throw new IllegalArgumentException("预期收获日期不能早于计划结束日期");
        }

        // 检查农田时间冲突（同一农田在同一时间段不能有多个计划）
        boolean hasConflict = plantingPlanRepository
            .existsByFarmlandIdAndPlannedStartDateLessThanEqualAndPlannedEndDateGreaterThanEqual(
                request.getFarmlandId(), request.getPlannedEndDate(), request.getPlannedStartDate());

        if (hasConflict) {
            throw new IllegalArgumentException("该农田在指定时间段内已有种植计划");
        }

        // 获取当前用户ID
        Long currentUserId = getCurrentUserId();

        PlantingPlan plantingPlan = new PlantingPlan();
        plantingPlan.setFarmlandId(request.getFarmlandId());
        plantingPlan.setCropId(request.getCropId());
        plantingPlan.setPlanName(request.getPlanName());
        plantingPlan.setPlannedStartDate(request.getPlannedStartDate());
        plantingPlan.setPlannedEndDate(request.getPlannedEndDate());
        plantingPlan.setExpectedHarvestDate(request.getExpectedHarvestDate());
        plantingPlan.setSowingDensity(request.getSowingDensity());
        plantingPlan.setNotes(request.getNotes());
        plantingPlan.setStatus(request.getStatus());
        plantingPlan.setCreatedBy(currentUserId);

        PlantingPlan savedPlantingPlan = plantingPlanRepository.save(plantingPlan);
        return convertToPlantingPlanResponse(savedPlantingPlan);
    }

    /**
     * 更新种植计划信息
     */
    @Transactional
    public PlantingPlanResponse updatePlantingPlan(Long id, PlantingPlanUpdateRequest request) {
        PlantingPlan plantingPlan = plantingPlanRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("种植计划不存在，ID: " + id));

        // 检查权限（只有创建者可以修改）
        Long currentUserId = getCurrentUserId();
        if (!plantingPlan.getCreatedBy().equals(currentUserId)) {
            throw new RuntimeException("无权修改此种植计划");
        }

        // 如果更新农田ID，验证农田存在性
        if (request.getFarmlandId() != null && !request.getFarmlandId().equals(plantingPlan.getFarmlandId())) {
            farmlandRepository.findById(request.getFarmlandId())
                .orElseThrow(() -> new ResourceNotFoundException("农田不存在，ID: " + request.getFarmlandId()));
        }

        // 如果更新作物ID，验证作物存在性
        if (request.getCropId() != null && !request.getCropId().equals(plantingPlan.getCropId())) {
            cropRepository.findById(request.getCropId())
                .orElseThrow(() -> new ResourceNotFoundException("作物不存在，ID: " + request.getCropId()));
        }

        // 验证日期逻辑
        LocalDate startDate = request.getPlannedStartDate() != null ? request.getPlannedStartDate() : plantingPlan.getPlannedStartDate();
        LocalDate endDate = request.getPlannedEndDate() != null ? request.getPlannedEndDate() : plantingPlan.getPlannedEndDate();

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("计划开始日期不能晚于计划结束日期");
        }

        LocalDate harvestDate = request.getExpectedHarvestDate() != null ? request.getExpectedHarvestDate() :
                               (plantingPlan.getExpectedHarvestDate() != null ? plantingPlan.getExpectedHarvestDate() : null);

        if (harvestDate != null && harvestDate.isBefore(endDate)) {
            throw new IllegalArgumentException("预期收获日期不能早于计划结束日期");
        }

        // 检查时间冲突（排除当前计划）
        Long farmlandId = request.getFarmlandId() != null ? request.getFarmlandId() : plantingPlan.getFarmlandId();
        if (!farmlandId.equals(plantingPlan.getFarmlandId()) || !startDate.equals(plantingPlan.getPlannedStartDate()) || !endDate.equals(plantingPlan.getPlannedEndDate())) {
            boolean hasConflict = plantingPlanRepository
                .existsByFarmlandIdAndCropIdAndPlannedStartDateLessThanEqualAndPlannedEndDateGreaterThanEqual(
                    farmlandId, plantingPlan.getCropId(), endDate, startDate);

            if (hasConflict) {
                throw new IllegalArgumentException("该农田在指定时间段内已有种植计划");
            }
        }

        // 更新字段
        if (request.getFarmlandId() != null) {
            plantingPlan.setFarmlandId(request.getFarmlandId());
        }
        if (request.getCropId() != null) {
            plantingPlan.setCropId(request.getCropId());
        }
        if (request.getPlanName() != null) {
            plantingPlan.setPlanName(request.getPlanName());
        }
        if (request.getPlannedStartDate() != null) {
            plantingPlan.setPlannedStartDate(request.getPlannedStartDate());
        }
        if (request.getPlannedEndDate() != null) {
            plantingPlan.setPlannedEndDate(request.getPlannedEndDate());
        }
        if (request.getExpectedHarvestDate() != null) {
            plantingPlan.setExpectedHarvestDate(request.getExpectedHarvestDate());
        }
        if (request.getSowingDensity() != null) {
            plantingPlan.setSowingDensity(request.getSowingDensity());
        }
        if (request.getNotes() != null) {
            plantingPlan.setNotes(request.getNotes());
        }
        if (request.getStatus() != null) {
            plantingPlan.setStatus(request.getStatus());
        }

        PlantingPlan updatedPlantingPlan = plantingPlanRepository.save(plantingPlan);
        return convertToPlantingPlanResponse(updatedPlantingPlan);
    }

    /**
     * 删除种植计划
     */
    @Transactional
    public void deletePlantingPlan(Long id) {
        PlantingPlan plantingPlan = plantingPlanRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("种植计划不存在，ID: " + id));

        // 检查权限（只有创建者可以删除）
        Long currentUserId = getCurrentUserId();
        if (!plantingPlan.getCreatedBy().equals(currentUserId)) {
            throw new RuntimeException("无权删除此种植计划");
        }

        plantingPlanRepository.delete(plantingPlan);
    }

    /**
     * 获取指定农田的种植计划列表
     */
    public List<PlantingPlanResponse> getPlantingPlansByFarmland(Long farmlandId) {
        // 验证农田存在
        farmlandRepository.findById(farmlandId)
            .orElseThrow(() -> new ResourceNotFoundException("农田不存在，ID: " + farmlandId));

        List<PlantingPlan> plantingPlans = plantingPlanRepository.findByFarmlandId(farmlandId);
        return plantingPlans.stream()
            .map(this::convertToPlantingPlanResponse)
            .collect(Collectors.toList());
    }

    /**
     * 获取指定作物的种植计划列表
     */
    public List<PlantingPlanResponse> getPlantingPlansByCrop(Long cropId) {
        // 验证作物存在
        cropRepository.findById(cropId)
            .orElseThrow(() -> new ResourceNotFoundException("作物不存在，ID: " + cropId));

        List<PlantingPlan> plantingPlans = plantingPlanRepository.findByCropId(cropId);
        return plantingPlans.stream()
            .map(this::convertToPlantingPlanResponse)
            .collect(Collectors.toList());
    }

    /**
     * 获取指定状态的种植计划列表
     */
    public List<PlantingPlanResponse> getPlantingPlansByStatus(PlantingPlan.PlantingStatus status) {
        List<PlantingPlan> plantingPlans = plantingPlanRepository.findByStatus(status);
        return plantingPlans.stream()
            .map(this::convertToPlantingPlanResponse)
            .collect(Collectors.toList());
    }

    /**
     * 获取当前用户的种植计划列表
     */
    public List<PlantingPlanResponse> getMyPlantingPlans() {
        Long currentUserId = getCurrentUserId();
        List<PlantingPlan> plantingPlans = plantingPlanRepository.findByCreatedBy(currentUserId);
        return plantingPlans.stream()
            .map(this::convertToPlantingPlanResponse)
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

    private PlantingPlanResponse convertToPlantingPlanResponse(PlantingPlan plantingPlan) {
        return new PlantingPlanResponse(
            plantingPlan.getId(),
            plantingPlan.getFarmlandId(),
            plantingPlan.getCropId(),
            plantingPlan.getPlanName(),
            plantingPlan.getPlannedStartDate(),
            plantingPlan.getPlannedEndDate(),
            plantingPlan.getExpectedHarvestDate(),
            plantingPlan.getSowingDensity(),
            plantingPlan.getNotes(),
            plantingPlan.getStatus(),
            plantingPlan.getCreatedBy(),
            plantingPlan.getCreatedAt(),
            plantingPlan.getUpdatedAt()
        );
    }
}
