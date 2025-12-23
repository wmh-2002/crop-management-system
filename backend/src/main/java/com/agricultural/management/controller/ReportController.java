package com.agricultural.management.controller;

import com.agricultural.management.dto.*;
import com.agricultural.management.entity.Report;
import com.agricultural.management.service.ReportService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    /**
     * 获取报告列表（分页）
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<PageResponse<ReportResponse>>> getReportList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String reportType,
            @RequestParam(required = false) Long generatedBy,
            @RequestParam(required = false) String startDateFrom,
            @RequestParam(required = false) String startDateTo,
            @RequestParam(required = false) String endDateFrom,
            @RequestParam(required = false) String endDateTo) {

        try {
            ReportQueryRequest request = new ReportQueryRequest();
            request.setPage(page);
            request.setSize(size);
            request.setTitle(title);
            request.setGeneratedBy(generatedBy);

            // 转换报告类型字符串为枚举
            if (reportType != null && !reportType.trim().isEmpty()) {
                try {
                    request.setReportType(Report.ReportType.valueOf(reportType.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid reportType: " + reportType);
                }
            }

            // 转换日期字符串为LocalDate
            if (startDateFrom != null && !startDateFrom.trim().isEmpty()) {
                try {
                    request.setStartDateFrom(LocalDate.parse(startDateFrom));
                } catch (Exception e) {
                    System.out.println("Invalid startDateFrom: " + startDateFrom);
                }
            }

            if (startDateTo != null && !startDateTo.trim().isEmpty()) {
                try {
                    request.setStartDateTo(LocalDate.parse(startDateTo));
                } catch (Exception e) {
                    System.out.println("Invalid startDateTo: " + startDateTo);
                }
            }

            if (endDateFrom != null && !endDateFrom.trim().isEmpty()) {
                try {
                    request.setEndDateFrom(LocalDate.parse(endDateFrom));
                } catch (Exception e) {
                    System.out.println("Invalid endDateFrom: " + endDateFrom);
                }
            }

            if (endDateTo != null && !endDateTo.trim().isEmpty()) {
                try {
                    request.setEndDateTo(LocalDate.parse(endDateTo));
                } catch (Exception e) {
                    System.out.println("Invalid endDateTo: " + endDateTo);
                }
            }

            PageResponse<ReportResponse> result = reportService.getReportList(request);
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            System.err.println("Error in getReportList: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<PageResponse<ReportResponse>>error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 根据ID获取报告详情
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<ReportResponse>> getReportById(@PathVariable Long id) {
        try {
            ReportResponse report = reportService.getReportById(id);
            return ResponseEntity.ok(ApiResponse.success(report));
        } catch (Exception e) {
            System.err.println("Error in getReportById: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<ReportResponse>error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 创建新报告
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<ReportResponse>> createReport(@Valid @RequestBody ReportCreateRequest request) {
        try {
            ReportResponse newReport = reportService.createReport(request);
            return ResponseEntity.status(201).body(ApiResponse.success("报告创建成功", newReport));
        } catch (Exception e) {
            System.err.println("Error in createReport: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<ReportResponse>error("创建报告失败: " + e.getMessage()));
        }
    }

    /**
     * 根据报告类型获取报告列表
     */
    @GetMapping("/type/{reportType}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<ReportResponse>>> getReportsByType(@PathVariable String reportType) {
        try {
            Report.ReportType type = Report.ReportType.valueOf(reportType.toUpperCase());
            List<ReportResponse> reports = reportService.getReportsByType(type);
            return ResponseEntity.ok(ApiResponse.success(reports));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.<List<ReportResponse>>error("无效的报告类型: " + reportType));
        } catch (Exception e) {
            System.err.println("Error in getReportsByType: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<List<ReportResponse>>error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 根据生成者获取报告列表
     */
    @GetMapping("/generated-by/{generatedBy}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<ReportResponse>>> getReportsByGeneratedBy(@PathVariable Long generatedBy) {
        try {
            List<ReportResponse> reports = reportService.getReportsByGeneratedBy(generatedBy);
            return ResponseEntity.ok(ApiResponse.success(reports));
        } catch (Exception e) {
            System.err.println("Error in getReportsByGeneratedBy: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<List<ReportResponse>>error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 获取当前用户的报告列表
     */
    @GetMapping("/my")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<ReportResponse>>> getMyReports() {
        try {
            List<ReportResponse> reports = reportService.getMyReports();
            return ResponseEntity.ok(ApiResponse.success(reports));
        } catch (Exception e) {
            System.err.println("Error in getMyReports: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<List<ReportResponse>>error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 根据日期范围获取报告列表
     */
    @GetMapping("/date-range")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<ReportResponse>>> getReportsByDateRange(
            @RequestParam String startDate, @RequestParam String endDate) {
        try {
            LocalDate start = LocalDate.parse(startDate);
            LocalDate end = LocalDate.parse(endDate);
            List<ReportResponse> reports = reportService.getReportsByDateRange(start, end);
            return ResponseEntity.ok(ApiResponse.success(reports));
        } catch (Exception e) {
            System.err.println("Error in getReportsByDateRange: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<List<ReportResponse>>error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 获取最新报告列表
     */
    @GetMapping("/latest")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<ReportResponse>>> getLatestReports(
            @RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<ReportResponse> reports = reportService.getLatestReports(limit);
            return ResponseEntity.ok(ApiResponse.success(reports));
        } catch (Exception e) {
            System.err.println("Error in getLatestReports: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<List<ReportResponse>>error("查询失败: " + e.getMessage()));
        }
    }
}
