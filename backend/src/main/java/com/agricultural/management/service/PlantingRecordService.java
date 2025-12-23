package com.agricultural.management.service;

import com.agricultural.management.dto.*;
import com.agricultural.management.entity.PlantingPlan;
import com.agricultural.management.entity.PlantingRecord;
import com.agricultural.management.entity.User;
import com.agricultural.management.exception.ResourceNotFoundException;
import com.agricultural.management.repository.PlantingPlanRepository;
import com.agricultural.management.repository.PlantingRecordRepository;
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
public class PlantingRecordService {

    private final PlantingRecordRepository plantingRecordRepository;
    private final PlantingPlanRepository plantingPlanRepository;
    private final UserRepository userRepository;

    public PlantingRecordService(PlantingRecordRepository plantingRecordRepository,
                               PlantingPlanRepository plantingPlanRepository,
                               UserRepository userRepository) {
        this.plantingRecordRepository = plantingRecordRepository;
        this.plantingPlanRepository = plantingPlanRepository;
        this.userRepository = userRepository;
    }

    /**
     * 分页查询种植记录列表
     */
    public PageResponse<PlantingRecordResponse> getPlantingRecordList(PlantingRecordQueryRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());

        Specification<PlantingRecord> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getPlanId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("planId"), request.getPlanId()));
            }

            if (request.getOperationType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("operationType"), request.getOperationType()));
            }

            if (request.getOperatorId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("operatorId"), request.getOperatorId()));
            }

            if (request.getOperationDateFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("operationDate"), request.getOperationDateFrom()));
            }

            if (request.getOperationDateTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("operationDate"), request.getOperationDateTo()));
            }

            if (request.getWeatherConditions() != null && !request.getWeatherConditions().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("weatherConditions"),
                    "%" + request.getWeatherConditions().trim() + "%"));
            }

            if (predicates.isEmpty()) {
                return null; // 没有查询条件，返回所有数据
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<PlantingRecord> plantingRecordPage = plantingRecordRepository.findAll(spec, pageable);

        List<PlantingRecordResponse> plantingRecordResponses = plantingRecordPage.getContent().stream()
            .map(this::convertToPlantingRecordResponse)
            .collect(Collectors.toList());

        return PageResponse.<PlantingRecordResponse>builder()
            .content(plantingRecordResponses)
            .page(plantingRecordPage.getNumber() + 1)
            .size(plantingRecordPage.getSize())
            .totalElements(plantingRecordPage.getTotalElements())
            .totalPages(plantingRecordPage.getTotalPages())
            .last(plantingRecordPage.isLast())
            .build();
    }

    /**
     * 根据ID获取种植记录信息
     */
    public PlantingRecordResponse getPlantingRecordById(Long id) {
        PlantingRecord plantingRecord = plantingRecordRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("种植记录不存在，ID: " + id));
        return convertToPlantingRecordResponse(plantingRecord);
    }

    /**
     * 创建新种植记录
     */
    @Transactional
    public PlantingRecordResponse createPlantingRecord(PlantingRecordCreateRequest request) {
        // 验证种植计划是否存在
        PlantingPlan plantingPlan = plantingPlanRepository.findById(request.getPlanId())
            .orElseThrow(() -> new ResourceNotFoundException("种植计划不存在，ID: " + request.getPlanId()));

        // 验证操作员存在性（如果指定了操作员）
        if (request.getOperatorId() != null) {
            userRepository.findById(request.getOperatorId())
                .orElseThrow(() -> new ResourceNotFoundException("操作员不存在，ID: " + request.getOperatorId()));
        }

        // 验证操作日期是否在种植计划的时间范围内
        if (request.getOperationDate().isBefore(plantingPlan.getPlannedStartDate()) ||
            request.getOperationDate().isAfter(plantingPlan.getPlannedEndDate())) {
            throw new IllegalArgumentException("操作日期必须在种植计划的时间范围内");
        }

        // 检查是否已经存在相同日期和类型的记录（避免重复记录）
        boolean exists = plantingRecordRepository.existsByPlanIdAndOperationDateAndOperationType(
            request.getPlanId(), request.getOperationDate(), request.getOperationType());

        if (exists) {
            throw new IllegalArgumentException("该种植计划在指定日期已存在相同类型的操作记录");
        }

        // 如果没有指定操作员，使用当前用户
        Long operatorId = request.getOperatorId() != null ? request.getOperatorId() : getCurrentUserId();

        PlantingRecord plantingRecord = new PlantingRecord();
        plantingRecord.setPlanId(request.getPlanId());
        plantingRecord.setOperationType(request.getOperationType());
        plantingRecord.setOperationDate(request.getOperationDate());
        plantingRecord.setQuantityUsed(request.getQuantityUsed());
        plantingRecord.setOperatorId(operatorId);
        plantingRecord.setWeatherConditions(request.getWeatherConditions());
        plantingRecord.setNotes(request.getNotes());

        PlantingRecord savedPlantingRecord = plantingRecordRepository.save(plantingRecord);
        return convertToPlantingRecordResponse(savedPlantingRecord);
    }

    /**
     * 更新种植记录信息
     */
    @Transactional
    public PlantingRecordResponse updatePlantingRecord(Long id, PlantingRecordUpdateRequest request) {
        PlantingRecord plantingRecord = plantingRecordRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("种植记录不存在，ID: " + id));

        // 检查权限（只有操作员本人可以修改记录）
        Long currentUserId = getCurrentUserId();
        if (!plantingRecord.getOperatorId().equals(currentUserId)) {
            throw new RuntimeException("无权修改此种植记录");
        }

        // 如果更新种植计划ID，验证种植计划存在性
        if (request.getPlanId() != null && !request.getPlanId().equals(plantingRecord.getPlanId())) {
            PlantingPlan plantingPlan = plantingPlanRepository.findById(request.getPlanId())
                .orElseThrow(() -> new ResourceNotFoundException("种植计划不存在，ID: " + request.getPlanId()));

            // 验证操作日期是否在新的种植计划时间范围内
            LocalDate operationDate = request.getOperationDate() != null ? request.getOperationDate() : plantingRecord.getOperationDate();
            if (operationDate.isBefore(plantingPlan.getPlannedStartDate()) ||
                operationDate.isAfter(plantingPlan.getPlannedEndDate())) {
                throw new IllegalArgumentException("操作日期必须在种植计划的时间范围内");
            }
        }

        // 如果更新操作员ID，验证操作员存在性
        if (request.getOperatorId() != null && !request.getOperatorId().equals(plantingRecord.getOperatorId())) {
            userRepository.findById(request.getOperatorId())
                .orElseThrow(() -> new ResourceNotFoundException("操作员不存在，ID: " + request.getOperatorId()));
        }

        // 检查唯一性约束（排除当前记录）
        LocalDate operationDate = request.getOperationDate() != null ? request.getOperationDate() : plantingRecord.getOperationDate();
        PlantingRecord.OperationType operationType = request.getOperationType() != null ? request.getOperationType() : plantingRecord.getOperationType();
        Long planId = request.getPlanId() != null ? request.getPlanId() : plantingRecord.getPlanId();

        if (!operationDate.equals(plantingRecord.getOperationDate()) ||
            !operationType.equals(plantingRecord.getOperationType()) ||
            !planId.equals(plantingRecord.getPlanId())) {

            boolean exists = plantingRecordRepository.existsByPlanIdAndOperationDateAndOperationType(
                planId, operationDate, operationType);

            if (exists) {
                throw new IllegalArgumentException("该种植计划在指定日期已存在相同类型的操作记录");
            }
        }

        // 更新字段
        if (request.getPlanId() != null) {
            plantingRecord.setPlanId(request.getPlanId());
        }
        if (request.getOperationType() != null) {
            plantingRecord.setOperationType(request.getOperationType());
        }
        if (request.getOperationDate() != null) {
            plantingRecord.setOperationDate(request.getOperationDate());
        }
        if (request.getQuantityUsed() != null) {
            plantingRecord.setQuantityUsed(request.getQuantityUsed());
        }
        if (request.getOperatorId() != null) {
            plantingRecord.setOperatorId(request.getOperatorId());
        }
        if (request.getWeatherConditions() != null) {
            plantingRecord.setWeatherConditions(request.getWeatherConditions());
        }
        if (request.getNotes() != null) {
            plantingRecord.setNotes(request.getNotes());
        }

        PlantingRecord updatedPlantingRecord = plantingRecordRepository.save(plantingRecord);
        return convertToPlantingRecordResponse(updatedPlantingRecord);
    }

    /**
     * 删除种植记录
     */
    @Transactional
    public void deletePlantingRecord(Long id) {
        PlantingRecord plantingRecord = plantingRecordRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("种植记录不存在，ID: " + id));

        // 检查权限（只有操作员本人可以删除记录）
        Long currentUserId = getCurrentUserId();
        if (!plantingRecord.getOperatorId().equals(currentUserId)) {
            throw new RuntimeException("无权删除此种植记录");
        }

        plantingRecordRepository.delete(plantingRecord);
    }

    /**
     * 获取指定种植计划的记录列表
     */
    public List<PlantingRecordResponse> getPlantingRecordsByPlan(Long planId) {
        // 验证种植计划存在
        plantingPlanRepository.findById(planId)
            .orElseThrow(() -> new ResourceNotFoundException("种植计划不存在，ID: " + planId));

        List<PlantingRecord> plantingRecords = plantingRecordRepository.findByPlanId(planId);
        return plantingRecords.stream()
            .map(this::convertToPlantingRecordResponse)
            .collect(Collectors.toList());
    }

    /**
     * 获取指定操作类型的记录列表
     */
    public List<PlantingRecordResponse> getPlantingRecordsByOperationType(PlantingRecord.OperationType operationType) {
        List<PlantingRecord> plantingRecords = plantingRecordRepository.findByOperationType(operationType);
        return plantingRecords.stream()
            .map(this::convertToPlantingRecordResponse)
            .collect(Collectors.toList());
    }

    /**
     * 获取当前用户的操作记录列表
     */
    public List<PlantingRecordResponse> getMyPlantingRecords() {
        Long currentUserId = getCurrentUserId();
        List<PlantingRecord> plantingRecords = plantingRecordRepository.findByOperatorId(currentUserId);
        return plantingRecords.stream()
            .map(this::convertToPlantingRecordResponse)
            .collect(Collectors.toList());
    }

    /**
     * 获取指定操作员的记录列表
     */
    public List<PlantingRecordResponse> getPlantingRecordsByOperator(Long operatorId) {
        // 验证操作员存在
        userRepository.findById(operatorId)
            .orElseThrow(() -> new ResourceNotFoundException("操作员不存在，ID: " + operatorId));

        List<PlantingRecord> plantingRecords = plantingRecordRepository.findByOperatorId(operatorId);
        return plantingRecords.stream()
            .map(this::convertToPlantingRecordResponse)
            .collect(Collectors.toList());
    }

    /**
     * 获取指定种植计划和操作类型的记录
     */
    public List<PlantingRecordResponse> getPlantingRecordsByPlanAndType(Long planId, PlantingRecord.OperationType operationType) {
        // 验证种植计划存在
        plantingPlanRepository.findById(planId)
            .orElseThrow(() -> new ResourceNotFoundException("种植计划不存在，ID: " + planId));

        List<PlantingRecord> plantingRecords = plantingRecordRepository.findByPlanIdAndOperationType(planId, operationType);
        return plantingRecords.stream()
            .map(this::convertToPlantingRecordResponse)
            .collect(Collectors.toList());
    }

    /**
     * 获取指定日期范围内的记录
     */
    public List<PlantingRecordResponse> getPlantingRecordsByDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("开始日期不能晚于结束日期");
        }

        List<PlantingRecord> plantingRecords = plantingRecordRepository.findByOperationDateBetween(startDate, endDate);
        return plantingRecords.stream()
            .map(this::convertToPlantingRecordResponse)
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

    private PlantingRecordResponse convertToPlantingRecordResponse(PlantingRecord plantingRecord) {
        return new PlantingRecordResponse(
            plantingRecord.getId(),
            plantingRecord.getPlanId(),
            plantingRecord.getOperationType(),
            plantingRecord.getOperationDate(),
            plantingRecord.getQuantityUsed(),
            plantingRecord.getOperatorId(),
            plantingRecord.getWeatherConditions(),
            plantingRecord.getNotes(),
            plantingRecord.getCreatedAt(),
            plantingRecord.getUpdatedAt()
        );
    }
}
