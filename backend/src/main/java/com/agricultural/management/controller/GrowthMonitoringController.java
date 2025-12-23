package com.agricultural.management.controller;

import com.agricultural.management.dto.*;
import com.agricultural.management.entity.GrowthMonitoring;
import com.agricultural.management.service.GrowthMonitoringService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/growth-monitoring")
@CrossOrigin(origins = "*", maxAge = 3600)
public class GrowthMonitoringController {

    private final GrowthMonitoringService growthMonitoringService;

    public GrowthMonitoringController(GrowthMonitoringService growthMonitoringService) {
        this.growthMonitoringService = growthMonitoringService;
    }

    /**
     * 获取生长监测列表（分页）
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<PageResponse<GrowthMonitoringResponse>>> getGrowthMonitoringList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) Long planId,
            @RequestParam(required = false) String monitoringDateFrom,
            @RequestParam(required = false) String monitoringDateTo,
            @RequestParam(required = false) String healthStatus,
            @RequestParam(required = false) Long createdBy,
            @RequestParam(required = false) BigDecimal minHeight,
            @RequestParam(required = false) BigDecimal maxHeight,
            @RequestParam(required = false) BigDecimal minWidth,
            @RequestParam(required = false) BigDecimal maxWidth,
            @RequestParam(required = false) BigDecimal minTemperature,
            @RequestParam(required = false) BigDecimal maxTemperature,
            @RequestParam(required = false) BigDecimal minHumidity,
            @RequestParam(required = false) BigDecimal maxHumidity,
            @RequestParam(required = false) BigDecimal minSoilMoisture,
            @RequestParam(required = false) BigDecimal maxSoilMoisture,
            @RequestParam(required = false) BigDecimal minPhLevel,
            @RequestParam(required = false) BigDecimal maxPhLevel) {

        try {
            GrowthMonitoringQueryRequest request = new GrowthMonitoringQueryRequest();
            request.setPage(page);
            request.setSize(size);
            request.setPlanId(planId);
            request.setCreatedBy(createdBy);
            request.setMinHeight(minHeight);
            request.setMaxHeight(maxHeight);
            request.setMinWidth(minWidth);
            request.setMaxWidth(maxWidth);
            request.setMinTemperature(minTemperature);
            request.setMaxTemperature(maxTemperature);
            request.setMinHumidity(minHumidity);
            request.setMaxHumidity(maxHumidity);
            request.setMinSoilMoisture(minSoilMoisture);
            request.setMaxSoilMoisture(maxSoilMoisture);
            request.setMinPhLevel(minPhLevel);
            request.setMaxPhLevel(maxPhLevel);

            // 转换健康状态字符串为枚举
            if (healthStatus != null && !healthStatus.trim().isEmpty()) {
                try {
                    request.setHealthStatus(GrowthMonitoring.HealthStatus.valueOf(healthStatus.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid healthStatus: " + healthStatus);
                }
            }

            // 转换日期字符串为LocalDate
            if (monitoringDateFrom != null && !monitoringDateFrom.trim().isEmpty()) {
                try {
                    request.setMonitoringDateFrom(LocalDate.parse(monitoringDateFrom));
                } catch (Exception e) {
                    System.out.println("Invalid monitoringDateFrom: " + monitoringDateFrom);
                }
            }

            if (monitoringDateTo != null && !monitoringDateTo.trim().isEmpty()) {
                try {
                    request.setMonitoringDateTo(LocalDate.parse(monitoringDateTo));
                } catch (Exception e) {
                    System.out.println("Invalid monitoringDateTo: " + monitoringDateTo);
                }
            }

            PageResponse<GrowthMonitoringResponse> result = growthMonitoringService.getGrowthMonitoringList(request);
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            System.err.println("Error in getGrowthMonitoringList: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<PageResponse<GrowthMonitoringResponse>>error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 根据ID获取生长监测信息
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<GrowthMonitoringResponse>> getGrowthMonitoringById(@PathVariable Long id) {
        try {
            GrowthMonitoringResponse growthMonitoring = growthMonitoringService.getGrowthMonitoringById(id);
            return ResponseEntity.ok(ApiResponse.success(growthMonitoring));
        } catch (Exception e) {
            System.err.println("Error in getGrowthMonitoringById: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<GrowthMonitoringResponse>error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 创建新生长监测记录
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<GrowthMonitoringResponse>> createGrowthMonitoring(@Valid @RequestBody GrowthMonitoringCreateRequest request) {
        try {
            GrowthMonitoringResponse newGrowthMonitoring = growthMonitoringService.createGrowthMonitoring(request);
            return ResponseEntity.status(201).body(ApiResponse.success("生长监测记录创建成功", newGrowthMonitoring));
        } catch (Exception e) {
            System.err.println("Error in createGrowthMonitoring: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<GrowthMonitoringResponse>error("创建生长监测记录失败: " + e.getMessage()));
        }
    }

    /**
     * 更新生长监测信息
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<GrowthMonitoringResponse>> updateGrowthMonitoring(@PathVariable Long id, @Valid @RequestBody GrowthMonitoringUpdateRequest request) {
        try {
            GrowthMonitoringResponse updatedGrowthMonitoring = growthMonitoringService.updateGrowthMonitoring(id, request);
            return ResponseEntity.ok(ApiResponse.success("生长监测记录更新成功", updatedGrowthMonitoring));
        } catch (Exception e) {
            System.err.println("Error in updateGrowthMonitoring: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<GrowthMonitoringResponse>error("更新生长监测记录失败: " + e.getMessage()));
        }
    }

    /**
     * 删除生长监测记录
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<String>> deleteGrowthMonitoring(@PathVariable Long id) {
        try {
            growthMonitoringService.deleteGrowthMonitoring(id);
            return ResponseEntity.ok(ApiResponse.success("生长监测记录删除成功"));
        } catch (Exception e) {
            System.err.println("Error in deleteGrowthMonitoring: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<String>error("删除生长监测记录失败: " + e.getMessage()));
        }
    }

    /**
     * 获取当前用户的生长监测记录列表
     */
    @GetMapping("/my")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<GrowthMonitoringResponse>>> getMyGrowthMonitoring() {
        try {
            List<GrowthMonitoringResponse> growthMonitorings = growthMonitoringService.getMyGrowthMonitoring();
            return ResponseEntity.ok(ApiResponse.success(growthMonitorings));
        } catch (Exception e) {
            System.err.println("Error in getMyGrowthMonitoring: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<List<GrowthMonitoringResponse>>error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 根据种植计划ID获取生长监测记录列表
     */
    @GetMapping("/plan/{planId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<GrowthMonitoringResponse>>> getGrowthMonitoringByPlan(@PathVariable Long planId) {
        try {
            List<GrowthMonitoringResponse> growthMonitorings = growthMonitoringService.getGrowthMonitoringByPlan(planId);
            return ResponseEntity.ok(ApiResponse.success(growthMonitorings));
        } catch (Exception e) {
            System.err.println("Error in getGrowthMonitoringByPlan: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<List<GrowthMonitoringResponse>>error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 根据种植计划和日期范围获取生长监测记录
     */
    @GetMapping("/plan/{planId}/date-range")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<GrowthMonitoringResponse>>> getGrowthMonitoringByPlanAndDateRange(
            @PathVariable Long planId,
            @RequestParam String startDate,
            @RequestParam String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            List<GrowthMonitoringResponse> growthMonitorings = growthMonitoringService.getGrowthMonitoringByPlanAndDateRange(planId, start, end);
            return ResponseEntity.ok(ApiResponse.success(growthMonitorings));
        } catch (Exception e) {
            System.err.println("Error in getGrowthMonitoringByPlanAndDateRange: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<List<GrowthMonitoringResponse>>error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 根据种植计划和健康状态获取生长监测记录
     */
    @GetMapping("/plan/{planId}/health-status/{healthStatus}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<GrowthMonitoringResponse>>> getGrowthMonitoringByPlanAndHealthStatus(
            @PathVariable Long planId, @PathVariable String healthStatus) {
        try {
            GrowthMonitoring.HealthStatus status = GrowthMonitoring.HealthStatus.valueOf(healthStatus.toUpperCase());
            List<GrowthMonitoringResponse> growthMonitorings = growthMonitoringService.getGrowthMonitoringByPlanAndHealthStatus(planId, status);
            return ResponseEntity.ok(ApiResponse.success(growthMonitorings));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.<List<GrowthMonitoringResponse>>error("无效的健康状态: " + healthStatus));
        } catch (Exception e) {
            System.err.println("Error in getGrowthMonitoringByPlanAndHealthStatus: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<List<GrowthMonitoringResponse>>error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 根据健康状态获取生长监测记录列表
     */
    @GetMapping("/health-status/{healthStatus}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<GrowthMonitoringResponse>>> getGrowthMonitoringByHealthStatus(@PathVariable String healthStatus) {
        try {
            GrowthMonitoring.HealthStatus status = GrowthMonitoring.HealthStatus.valueOf(healthStatus.toUpperCase());
            List<GrowthMonitoringResponse> growthMonitorings = growthMonitoringService.getGrowthMonitoringByHealthStatus(status);
            return ResponseEntity.ok(ApiResponse.success(growthMonitorings));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.<List<GrowthMonitoringResponse>>error("无效的健康状态: " + healthStatus));
        } catch (Exception e) {
            System.err.println("Error in getGrowthMonitoringByHealthStatus: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<List<GrowthMonitoringResponse>>error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 根据日期范围获取生长监测记录
     */
    @GetMapping("/date-range")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<GrowthMonitoringResponse>>> getGrowthMonitoringByDateRange(
            @RequestParam String startDate, @RequestParam String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            List<GrowthMonitoringResponse> growthMonitorings = growthMonitoringService.getGrowthMonitoringByDateRange(start, end);
            return ResponseEntity.ok(ApiResponse.success(growthMonitorings));
        } catch (Exception e) {
            System.err.println("Error in getGrowthMonitoringByDateRange: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<List<GrowthMonitoringResponse>>error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 根据温度范围获取生长监测记录
     */
    @GetMapping("/temperature-range")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<GrowthMonitoringResponse>>> getGrowthMonitoringByTemperatureRange(
            @RequestParam BigDecimal minTemp, @RequestParam BigDecimal maxTemp) {
        try {
            List<GrowthMonitoringResponse> growthMonitorings = growthMonitoringService.getGrowthMonitoringByTemperatureRange(minTemp, maxTemp);
            return ResponseEntity.ok(ApiResponse.success(growthMonitorings));
        } catch (Exception e) {
            System.err.println("Error in getGrowthMonitoringByTemperatureRange: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<List<GrowthMonitoringResponse>>error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 根据土壤湿度范围获取生长监测记录
     */
    @GetMapping("/soil-moisture-range")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<GrowthMonitoringResponse>>> getGrowthMonitoringBySoilMoistureRange(
            @RequestParam BigDecimal minMoisture, @RequestParam BigDecimal maxMoisture) {
        try {
            List<GrowthMonitoringResponse> growthMonitorings = growthMonitoringService.getGrowthMonitoringBySoilMoistureRange(minMoisture, maxMoisture);
            return ResponseEntity.ok(ApiResponse.success(growthMonitorings));
        } catch (Exception e) {
            System.err.println("Error in getGrowthMonitoringBySoilMoistureRange: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<List<GrowthMonitoringResponse>>error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 根据pH值范围获取生长监测记录
     */
    @GetMapping("/ph-range")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<GrowthMonitoringResponse>>> getGrowthMonitoringByPhRange(
            @RequestParam BigDecimal minPh, @RequestParam BigDecimal maxPh) {
        try {
            List<GrowthMonitoringResponse> growthMonitorings = growthMonitoringService.getGrowthMonitoringByPhRange(minPh, maxPh);
            return ResponseEntity.ok(ApiResponse.success(growthMonitorings));
        } catch (Exception e) {
            System.err.println("Error in getGrowthMonitoringByPhRange: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<List<GrowthMonitoringResponse>>error("查询失败: " + e.getMessage()));
        }
    }
}
