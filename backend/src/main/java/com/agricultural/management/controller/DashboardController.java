package com.agricultural.management.controller;

import com.agricultural.management.dto.ApiResponse;
import com.agricultural.management.dto.DashboardResponse;
import com.agricultural.management.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    /**
     * 获取仪表板统计数据
     */
    @GetMapping("/statistics")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<DashboardResponse>> getDashboardStatistics() {
        try {
            DashboardResponse dashboardData = dashboardService.getDashboardData();
            return ResponseEntity.ok(ApiResponse.success(dashboardData));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(ApiResponse.<DashboardResponse>error("获取仪表板数据失败: " + e.getMessage()));
        }
    }
}
