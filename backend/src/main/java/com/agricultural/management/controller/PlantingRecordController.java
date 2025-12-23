package com.agricultural.management.controller;

import com.agricultural.management.dto.*;
import com.agricultural.management.entity.PlantingRecord;
import com.agricultural.management.service.PlantingRecordService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/planting-records")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PlantingRecordController {

    private final PlantingRecordService plantingRecordService;

    public PlantingRecordController(PlantingRecordService plantingRecordService) {
        this.plantingRecordService = plantingRecordService;
    }

    /**
     * 获取种植记录列表（分页）
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<PageResponse<PlantingRecordResponse>>> getPlantingRecordList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long planId,
            @RequestParam(required = false) String operationType,
            @RequestParam(required = false) Long operatorId,
            @RequestParam(required = false) String operationDateFrom,
            @RequestParam(required = false) String operationDateTo,
            @RequestParam(required = false) String weatherConditions) {

        try {
            PlantingRecordQueryRequest request = new PlantingRecordQueryRequest();
            request.setPage(page);
            request.setSize(size);
            request.setPlanId(planId);
            request.setOperatorId(operatorId);
            request.setWeatherConditions(weatherConditions);

            // 转换操作类型字符串为枚举
            if (operationType != null && !operationType.trim().isEmpty()) {
                try {
                    request.setOperationType(PlantingRecord.OperationType.valueOf(operationType.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid operationType: " + operationType);
                }
            }

            // 转换日期字符串为LocalDate
            if (operationDateFrom != null && !operationDateFrom.trim().isEmpty()) {
                try {
                    request.setOperationDateFrom(LocalDate.parse(operationDateFrom));
                } catch (Exception e) {
                    System.out.println("Invalid operationDateFrom: " + operationDateFrom);
                }
            }

            if (operationDateTo != null && !operationDateTo.trim().isEmpty()) {
                try {
                    request.setOperationDateTo(LocalDate.parse(operationDateTo));
                } catch (Exception e) {
                    System.out.println("Invalid operationDateTo: " + operationDateTo);
                }
            }

            PageResponse<PlantingRecordResponse> result = plantingRecordService.getPlantingRecordList(request);
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            System.err.println("Error in getPlantingRecordList: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<PageResponse<PlantingRecordResponse>>error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 根据ID获取种植记录信息
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<PlantingRecordResponse>> getPlantingRecordById(@PathVariable Long id) {
        try {
            PlantingRecordResponse plantingRecord = plantingRecordService.getPlantingRecordById(id);
            return ResponseEntity.ok(ApiResponse.success(plantingRecord));
        } catch (Exception e) {
            System.err.println("Error in getPlantingRecordById: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<PlantingRecordResponse>error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 创建新种植记录
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<PlantingRecordResponse>> createPlantingRecord(@Valid @RequestBody PlantingRecordCreateRequest request) {
        try {
            PlantingRecordResponse newPlantingRecord = plantingRecordService.createPlantingRecord(request);
            return ResponseEntity.status(201).body(ApiResponse.success("种植记录创建成功", newPlantingRecord));
        } catch (Exception e) {
            System.err.println("Error in createPlantingRecord: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<PlantingRecordResponse>error("创建种植记录失败: " + e.getMessage()));
        }
    }

    /**
     * 更新种植记录信息
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<PlantingRecordResponse>> updatePlantingRecord(@PathVariable Long id, @Valid @RequestBody PlantingRecordUpdateRequest request) {
        try {
            PlantingRecordResponse updatedPlantingRecord = plantingRecordService.updatePlantingRecord(id, request);
            return ResponseEntity.ok(ApiResponse.success("种植记录更新成功", updatedPlantingRecord));
        } catch (Exception e) {
            System.err.println("Error in updatePlantingRecord: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<PlantingRecordResponse>error("更新种植记录失败: " + e.getMessage()));
        }
    }

    /**
     * 删除种植记录
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<String>> deletePlantingRecord(@PathVariable Long id) {
        try {
            plantingRecordService.deletePlantingRecord(id);
            return ResponseEntity.ok(ApiResponse.success("种植记录删除成功"));
        } catch (Exception e) {
            System.err.println("Error in deletePlantingRecord: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<String>error("删除种植记录失败: " + e.getMessage()));
        }
    }

    /**
     * 获取当前用户的种植记录列表
     */
    @GetMapping("/my")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<PlantingRecordResponse>>> getMyPlantingRecords() {
        try {
            List<PlantingRecordResponse> plantingRecords = plantingRecordService.getMyPlantingRecords();
            return ResponseEntity.ok(ApiResponse.success(plantingRecords));
        } catch (Exception e) {
            System.err.println("Error in getMyPlantingRecords: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<List<PlantingRecordResponse>>error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 根据种植计划ID获取记录列表
     */
    @GetMapping("/plan/{planId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<PlantingRecordResponse>>> getPlantingRecordsByPlan(@PathVariable Long planId) {
        try {
            List<PlantingRecordResponse> plantingRecords = plantingRecordService.getPlantingRecordsByPlan(planId);
            return ResponseEntity.ok(ApiResponse.success(plantingRecords));
        } catch (Exception e) {
            System.err.println("Error in getPlantingRecordsByPlan: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<List<PlantingRecordResponse>>error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 根据操作类型获取记录列表
     */
    @GetMapping("/operation-type/{operationType}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<PlantingRecordResponse>>> getPlantingRecordsByOperationType(@PathVariable String operationType) {
        try {
            PlantingRecord.OperationType type = PlantingRecord.OperationType.valueOf(operationType.toUpperCase());
            List<PlantingRecordResponse> plantingRecords = plantingRecordService.getPlantingRecordsByOperationType(type);
            return ResponseEntity.ok(ApiResponse.success(plantingRecords));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.<List<PlantingRecordResponse>>error("无效的操作类型: " + operationType));
        } catch (Exception e) {
            System.err.println("Error in getPlantingRecordsByOperationType: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<List<PlantingRecordResponse>>error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 根据操作员ID获取记录列表
     */
    @GetMapping("/operator/{operatorId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<PlantingRecordResponse>>> getPlantingRecordsByOperator(@PathVariable Long operatorId) {
        try {
            List<PlantingRecordResponse> plantingRecords = plantingRecordService.getPlantingRecordsByOperator(operatorId);
            return ResponseEntity.ok(ApiResponse.success(plantingRecords));
        } catch (Exception e) {
            System.err.println("Error in getPlantingRecordsByOperator: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<List<PlantingRecordResponse>>error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 根据种植计划和操作类型获取记录
     */
    @GetMapping("/plan/{planId}/operation-type/{operationType}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<PlantingRecordResponse>>> getPlantingRecordsByPlanAndType(
            @PathVariable Long planId, @PathVariable String operationType) {
        try {
            PlantingRecord.OperationType type = PlantingRecord.OperationType.valueOf(operationType.toUpperCase());
            List<PlantingRecordResponse> plantingRecords = plantingRecordService.getPlantingRecordsByPlanAndType(planId, type);
            return ResponseEntity.ok(ApiResponse.success(plantingRecords));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.<List<PlantingRecordResponse>>error("无效的操作类型: " + operationType));
        } catch (Exception e) {
            System.err.println("Error in getPlantingRecordsByPlanAndType: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<List<PlantingRecordResponse>>error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 根据日期范围获取记录
     */
    @GetMapping("/date-range")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<PlantingRecordResponse>>> getPlantingRecordsByDateRange(
            @RequestParam String startDate, @RequestParam String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            List<PlantingRecordResponse> plantingRecords = plantingRecordService.getPlantingRecordsByDateRange(start, end);
            return ResponseEntity.ok(ApiResponse.success(plantingRecords));
        } catch (Exception e) {
            System.err.println("Error in getPlantingRecordsByDateRange: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<List<PlantingRecordResponse>>error("查询失败: " + e.getMessage()));
        }
    }
}
