package com.agricultural.management.controller;

import com.agricultural.management.dto.ApiResponse;
import com.agricultural.management.dto.DataAnalysisResponse;
import com.agricultural.management.service.DataAnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analysis")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DataAnalysisController {

    private final DataAnalysisService dataAnalysisService;

    public DataAnalysisController(DataAnalysisService dataAnalysisService) {
        this.dataAnalysisService = dataAnalysisService;
    }

    /**
     * 获取作物生长分析数据
     */
    @GetMapping("/crop-growth")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<DataAnalysisResponse.CropGrowthAnalysisData>> getCropGrowthAnalysis() {
        try {
            DataAnalysisResponse.CropGrowthAnalysisData data = dataAnalysisService.getAllAnalysisData().getCropGrowthAnalysis();
            return ResponseEntity.ok(ApiResponse.success(data));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(ApiResponse.<DataAnalysisResponse.CropGrowthAnalysisData>error("获取作物生长分析数据失败: " + e.getMessage()));
        }
    }

    /**
     * 获取农田利用率分析数据
     */
    @GetMapping("/field-utilization")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<DataAnalysisResponse.FieldUtilizationAnalysisData>> getFieldUtilizationAnalysis() {
        try {
            DataAnalysisResponse.FieldUtilizationAnalysisData data = dataAnalysisService.getAllAnalysisData().getFieldUtilizationAnalysis();
            return ResponseEntity.ok(ApiResponse.success(data));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(ApiResponse.<DataAnalysisResponse.FieldUtilizationAnalysisData>error("获取农田利用率分析数据失败: " + e.getMessage()));
        }
    }

    /**
     * 获取综合统计数据
     */
    @GetMapping("/comprehensive")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<DataAnalysisResponse.ComprehensiveStatisticsData>> getComprehensiveStatistics() {
        try {
            DataAnalysisResponse.ComprehensiveStatisticsData data = dataAnalysisService.getAllAnalysisData().getComprehensiveStatistics();
            return ResponseEntity.ok(ApiResponse.success(data));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(ApiResponse.<DataAnalysisResponse.ComprehensiveStatisticsData>error("获取综合统计数据失败: " + e.getMessage()));
        }
    }

    /**
     * 获取所有分析数据
     */
    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponse<DataAnalysisResponse>> getAllAnalysisData() {
        try {
            DataAnalysisResponse data = dataAnalysisService.getAllAnalysisData();
            return ResponseEntity.ok(ApiResponse.success(data));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body(ApiResponse.<DataAnalysisResponse>error("获取分析数据失败: " + e.getMessage()));
        }
    }
}
