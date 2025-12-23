package com.agricultural.management.service;

import com.agricultural.management.dto.*;
import com.agricultural.management.entity.Farmland;
import com.agricultural.management.entity.User;
import com.agricultural.management.exception.DuplicateEntryException;
import com.agricultural.management.exception.ResourceNotFoundException;
import com.agricultural.management.repository.FarmlandRepository;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FarmlandService {

    private final FarmlandRepository farmlandRepository;
    private final UserRepository userRepository;

    public FarmlandService(FarmlandRepository farmlandRepository, UserRepository userRepository) {
        this.farmlandRepository = farmlandRepository;
        this.userRepository = userRepository;
    }

    /**
     * 分页查询农田列表
     */
    public PageResponse<FarmlandResponse> getFarmlandList(FarmlandQueryRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());

        Specification<Farmland> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getName() != null && !request.getName().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("name"),
                    "%" + request.getName().trim() + "%"));
            }

            if (request.getLocation() != null && !request.getLocation().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("location"),
                    "%" + request.getLocation().trim() + "%"));
            }

            if (request.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), request.getStatus()));
            }

            if (request.getCreatedBy() != null) {
                predicates.add(criteriaBuilder.equal(root.get("createdBy"), request.getCreatedBy()));
            }

            if (predicates.isEmpty()) {
                return null; // 没有查询条件，返回所有数据
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<Farmland> farmlandPage = farmlandRepository.findAll(spec, pageable);

        List<FarmlandResponse> farmlandResponses = farmlandPage.getContent().stream()
            .map(this::convertToFarmlandResponse)
            .collect(Collectors.toList());

        return PageResponse.<FarmlandResponse>builder()
            .content(farmlandResponses)
            .page(farmlandPage.getNumber() + 1)
            .size(farmlandPage.getSize())
            .totalElements(farmlandPage.getTotalElements())
            .totalPages(farmlandPage.getTotalPages())
            .last(farmlandPage.isLast())
            .build();
    }

    /**
     * 根据ID获取农田信息
     */
    public FarmlandResponse getFarmlandById(Long id) {
        Farmland farmland = farmlandRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("农田不存在，ID: " + id));
        return convertToFarmlandResponse(farmland);
    }

    /**
     * 创建新农田
     */
    @Transactional
    public FarmlandResponse createFarmland(FarmlandCreateRequest request) {
        // 获取当前登录用户
        Long currentUserId = getCurrentUserId();

        // 检查农田名称是否已存在（同一用户下）
        if (farmlandRepository.existsByNameAndCreatedBy(request.getName(), currentUserId)) {
            throw new DuplicateEntryException("农田名称已存在: " + request.getName());
        }

        Farmland farmland = new Farmland();
        farmland.setName(request.getName());
        farmland.setArea(request.getArea());
        farmland.setLocation(request.getLocation());
        farmland.setStatus(request.getStatus());
        farmland.setDescription(request.getDescription());
        farmland.setCreatedBy(currentUserId);

        Farmland savedFarmland = farmlandRepository.save(farmland);
        return convertToFarmlandResponse(savedFarmland);
    }

    /**
     * 更新农田信息
     */
    @Transactional
    public FarmlandResponse updateFarmland(Long id, FarmlandUpdateRequest request) {
        Farmland farmland = farmlandRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("农田不存在，ID: " + id));

        // 检查权限（只有创建者可以修改）
        Long currentUserId = getCurrentUserId();
        if (!farmland.getCreatedBy().equals(currentUserId)) {
            throw new RuntimeException("无权修改此农田信息");
        }

        // 检查农田名称是否与其他农田重复
        if (request.getName() != null && !request.getName().equals(farmland.getName())) {
            if (farmlandRepository.existsByNameAndCreatedByAndIdNot(request.getName(), currentUserId, id)) {
                throw new DuplicateEntryException("农田名称已存在: " + request.getName());
            }
            farmland.setName(request.getName());
        }

        if (request.getArea() != null) {
            farmland.setArea(request.getArea());
        }
        if (request.getLocation() != null) {
            farmland.setLocation(request.getLocation());
        }
        if (request.getStatus() != null) {
            farmland.setStatus(request.getStatus());
        }
        if (request.getDescription() != null) {
            farmland.setDescription(request.getDescription());
        }

        Farmland updatedFarmland = farmlandRepository.save(farmland);
        return convertToFarmlandResponse(updatedFarmland);
    }

    /**
     * 删除农田
     */
    @Transactional
    public void deleteFarmland(Long id) {
        Farmland farmland = farmlandRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("农田不存在，ID: " + id));

        // 检查权限（只有创建者可以删除）
        Long currentUserId = getCurrentUserId();
        if (!farmland.getCreatedBy().equals(currentUserId)) {
            throw new RuntimeException("无权删除此农田");
        }

        farmlandRepository.delete(farmland);
    }

    /**
     * 获取当前用户的农田列表
     */
    public List<FarmlandResponse> getMyFarmlands() {
        Long currentUserId = getCurrentUserId();
        List<Farmland> farmlands = farmlandRepository.findByCreatedBy(currentUserId);
        return farmlands.stream()
            .map(this::convertToFarmlandResponse)
            .collect(Collectors.toList());
    }

    /**
     * 获取指定状态的农田列表
     */
    public List<FarmlandResponse> getFarmlandsByStatus(Farmland.FarmlandStatus status) {
        List<Farmland> farmlands = farmlandRepository.findByStatus(status);
        return farmlands.stream()
            .map(this::convertToFarmlandResponse)
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

    private FarmlandResponse convertToFarmlandResponse(Farmland farmland) {
        return new FarmlandResponse(
            farmland.getId(),
            farmland.getName(),
            farmland.getArea(),
            farmland.getLocation(),
            farmland.getStatus(),
            farmland.getDescription(),
            farmland.getCreatedBy(),
            farmland.getCreatedAt(),
            farmland.getUpdatedAt()
        );
    }
}
