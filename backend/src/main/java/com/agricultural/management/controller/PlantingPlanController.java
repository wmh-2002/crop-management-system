package com.agricultural.management.controller;

import com.agricultural.management.dto.*;
import com.agricultural.management.entity.PlantingPlan;
import com.agricultural.management.service.PlantingPlanService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/planting-plans")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PlantingPlanController {

    private final PlantingPlanService plantingPlanService;

    public PlantingPlanController(PlantingPlanService plantingPlanService) {
        this.plantingPlanService = plantingPlanService;
    }

    /**
     * 获取种植计划列表（分页）
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<PageResponse<PlantingPlanResponse>>> getPlantingPlanList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String planName,
            @RequestParam(required = false) Long farmlandId,
            @RequestParam(required = false) Long cropId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long createdBy,
            @RequestParam(required = false) String startDateFrom,
            @RequestParam(required = false) String startDateTo,
            @RequestParam(required = false) String endDateFrom,
            @RequestParam(required = false) String endDateTo) {

        try {
            PlantingPlanQueryRequest request = new PlantingPlanQueryRequest();
            request.setPage(page);
            request.setSize(size);
            request.setPlanName(planName);
            request.setFarmlandId(farmlandId);
            request.setCropId(cropId);

            // 转换状态字符串为枚举
            if (status != null && !status.trim().isEmpty()) {
                try {
                    request.setStatus(PlantingPlan.PlantingStatus.valueOf(status.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid status: " + status);
                }
            }

            request.setCreatedBy(createdBy);

            // 转换日期字符串为LocalDate
            if (startDateFrom != null && !startDateFrom.trim().isEmpty()) {
                try {
                    request.setStartDateFrom(java.time.LocalDate.parse(startDateFrom));
                } catch (Exception e) {
                    System.out.println("Invalid startDateFrom: " + startDateFrom);
                }
            }

            if (startDateTo != null && !startDateTo.trim().isEmpty()) {
                try {
                    request.setStartDateTo(java.time.LocalDate.parse(startDateTo));
                } catch (Exception e) {
                    System.out.println("Invalid startDateTo: " + startDateTo);
                }
            }

            if (endDateFrom != null && !endDateFrom.trim().isEmpty()) {
                try {
                    request.setEndDateFrom(java.time.LocalDate.parse(endDateFrom));
                } catch (Exception e) {
                    System.out.println("Invalid endDateFrom: " + endDateFrom);
                }
            }

            if (endDateTo != null && !endDateTo.trim().isEmpty()) {
                try {
                    request.setEndDateTo(java.time.LocalDate.parse(endDateTo));
                } catch (Exception e) {
                    System.out.println("Invalid endDateTo: " + endDateTo);
                }
            }

            PageResponse<PlantingPlanResponse> result = plantingPlanService.getPlantingPlanList(request);
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            System.err.println("Error in getPlantingPlanList: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<PageResponse<PlantingPlanResponse>>error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 根据ID获取种植计划信息
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<PlantingPlanResponse>> getPlantingPlanById(@PathVariable Long id) {
        try {
            PlantingPlanResponse plantingPlan = plantingPlanService.getPlantingPlanById(id);
            return ResponseEntity.ok(ApiResponse.success(plantingPlan));
        } catch (Exception e) {
            System.err.println("Error in getPlantingPlanById: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<PlantingPlanResponse>error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 创建新种植计划
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<PlantingPlanResponse>> createPlantingPlan(@Valid @RequestBody PlantingPlanCreateRequest request) {
        try {
            PlantingPlanResponse newPlantingPlan = plantingPlanService.createPlantingPlan(request);
            return ResponseEntity.status(201).body(ApiResponse.success("种植计划创建成功", newPlantingPlan));
        } catch (Exception e) {
            System.err.println("Error in createPlantingPlan: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<PlantingPlanResponse>error("创建种植计划失败: " + e.getMessage()));
        }
    }

    /**
     * 更新种植计划信息
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<PlantingPlanResponse>> updatePlantingPlan(@PathVariable Long id, @Valid @RequestBody PlantingPlanUpdateRequest request) {
        try {
            PlantingPlanResponse updatedPlantingPlan = plantingPlanService.updatePlantingPlan(id, request);
            return ResponseEntity.ok(ApiResponse.success("种植计划更新成功", updatedPlantingPlan));
        } catch (Exception e) {
            System.err.println("Error in updatePlantingPlan: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<PlantingPlanResponse>error("更新种植计划失败: " + e.getMessage()));
        }
    }

    /**
     * 删除种植计划
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<String>> deletePlantingPlan(@PathVariable Long id) {
        try {
            plantingPlanService.deletePlantingPlan(id);
            return ResponseEntity.ok(ApiResponse.success("种植计划删除成功"));
        } catch (Exception e) {
            System.err.println("Error in deletePlantingPlan: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<String>error("删除种植计划失败: " + e.getMessage()));
        }
    }

    /**
     * 获取当前用户的种植计划列表
     */
    @GetMapping("/my")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<PlantingPlanResponse>>> getMyPlantingPlans() {
        try {
            List<PlantingPlanResponse> plantingPlans = plantingPlanService.getMyPlantingPlans();
            return ResponseEntity.ok(ApiResponse.success(plantingPlans));
        } catch (Exception e) {
            System.err.println("Error in getMyPlantingPlans: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<List<PlantingPlanResponse>>error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 根据农田ID获取种植计划列表
     */
    @GetMapping("/farmland/{farmlandId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<PlantingPlanResponse>>> getPlantingPlansByFarmland(@PathVariable Long farmlandId) {
        try {
            List<PlantingPlanResponse> plantingPlans = plantingPlanService.getPlantingPlansByFarmland(farmlandId);
            return ResponseEntity.ok(ApiResponse.success(plantingPlans));
        } catch (Exception e) {
            System.err.println("Error in getPlantingPlansByFarmland: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<List<PlantingPlanResponse>>error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 根据作物ID获取种植计划列表
     */
    @GetMapping("/crop/{cropId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<PlantingPlanResponse>>> getPlantingPlansByCrop(@PathVariable Long cropId) {
        try {
            List<PlantingPlanResponse> plantingPlans = plantingPlanService.getPlantingPlansByCrop(cropId);
            return ResponseEntity.ok(ApiResponse.success(plantingPlans));
        } catch (Exception e) {
            System.err.println("Error in getPlantingPlansByCrop: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<List<PlantingPlanResponse>>error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 根据状态获取种植计划列表
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<PlantingPlanResponse>>> getPlantingPlansByStatus(@PathVariable String status) {
        try {
            PlantingPlan.PlantingStatus plantingStatus = PlantingPlan.PlantingStatus.valueOf(status.toUpperCase());
            List<PlantingPlanResponse> plantingPlans = plantingPlanService.getPlantingPlansByStatus(plantingStatus);
            return ResponseEntity.ok(ApiResponse.success(plantingPlans));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.<List<PlantingPlanResponse>>error("无效的状态值: " + status));
        } catch (Exception e) {
            System.err.println("Error in getPlantingPlansByStatus: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<List<PlantingPlanResponse>>error("查询失败: " + e.getMessage()));
        }
    }
}
