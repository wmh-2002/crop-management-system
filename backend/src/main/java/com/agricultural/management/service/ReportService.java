package com.agricultural.management.service;

import com.agricultural.management.dto.*;
import com.agricultural.management.entity.Report;
import com.agricultural.management.entity.User;
import com.agricultural.management.exception.ResourceNotFoundException;
import com.agricultural.management.repository.ReportRepository;
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
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    public ReportService(ReportRepository reportRepository, UserRepository userRepository) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
    }

    /**
     * 分页查询报告列表
     */
    public PageResponse<ReportResponse> getReportList(ReportQueryRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());

        Specification<Report> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getTitle() != null && !request.getTitle().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("title"),
                    "%" + request.getTitle().trim() + "%"));
            }

            if (request.getReportType() != null) {
                predicates.add(criteriaBuilder.equal(root.get("reportType"), request.getReportType()));
            }

            if (request.getGeneratedBy() != null) {
                predicates.add(criteriaBuilder.equal(root.get("generatedBy"), request.getGeneratedBy()));
            }

            if (request.getStartDateFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), request.getStartDateFrom()));
            }

            if (request.getStartDateTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("startDate"), request.getStartDateTo()));
            }

            if (request.getEndDateFrom() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("endDate"), request.getEndDateFrom()));
            }

            if (request.getEndDateTo() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), request.getEndDateTo()));
            }

            if (predicates.isEmpty()) {
                return null; // 没有查询条件，返回所有数据
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<Report> reportPage = reportRepository.findAll(spec, pageable);

        List<ReportResponse> reportResponses = reportPage.getContent().stream()
            .map(this::convertToReportResponse)
            .collect(Collectors.toList());

        return PageResponse.<ReportResponse>builder()
            .content(reportResponses)
            .page(reportPage.getNumber() + 1)
            .size(reportPage.getSize())
            .totalElements(reportPage.getTotalElements())
            .totalPages(reportPage.getTotalPages())
            .last(reportPage.isLast())
            .build();
    }

    /**
     * 根据ID获取报告信息
     */
    public ReportResponse getReportById(Long id) {
        Report report = reportRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("报告不存在，ID: " + id));
        return convertToReportResponse(report);
    }

    /**
     * 创建新报告
     */
    @Transactional
    public ReportResponse createReport(ReportCreateRequest request) {
        // 验证日期逻辑
        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new IllegalArgumentException("开始日期不能晚于结束日期");
        }

        // 验证生成者存在性（如果指定了生成者）
        if (request.getGeneratedBy() != null) {
            userRepository.findById(request.getGeneratedBy())
                .orElseThrow(() -> new ResourceNotFoundException("生成者用户不存在，ID: " + request.getGeneratedBy()));
        }

        // 获取当前用户ID（如果未指定生成者）
        Long generatedBy = request.getGeneratedBy() != null ? request.getGeneratedBy() : getCurrentUserId();

        Report report = new Report();
        report.setTitle(request.getTitle());
        report.setReportType(request.getReportType());
        report.setStartDate(request.getStartDate());
        report.setEndDate(request.getEndDate());
        report.setContent(request.getContent());
        report.setChartData(request.getChartData());
        report.setGeneratedBy(generatedBy);

        Report savedReport = reportRepository.save(report);
        return convertToReportResponse(savedReport);
    }

    /**
     * 根据报告类型获取报告列表
     */
    public List<ReportResponse> getReportsByType(Report.ReportType reportType) {
        List<Report> reports = reportRepository.findByReportTypeOrderByCreatedAtDesc(reportType);
        return reports.stream()
            .map(this::convertToReportResponse)
            .collect(Collectors.toList());
    }

    /**
     * 根据生成者获取报告列表
     */
    public List<ReportResponse> getReportsByGeneratedBy(Long generatedBy) {
        // 验证用户存在
        userRepository.findById(generatedBy)
            .orElseThrow(() -> new ResourceNotFoundException("用户不存在，ID: " + generatedBy));

        List<Report> reports = reportRepository.findByGeneratedByOrderByCreatedAtDesc(generatedBy);
        return reports.stream()
            .map(this::convertToReportResponse)
            .collect(Collectors.toList());
    }

    /**
     * 获取当前用户的报告列表
     */
    public List<ReportResponse> getMyReports() {
        Long currentUserId = getCurrentUserId();
        List<Report> reports = reportRepository.findByGeneratedByOrderByCreatedAtDesc(currentUserId);
        return reports.stream()
            .map(this::convertToReportResponse)
            .collect(Collectors.toList());
    }

    /**
     * 根据日期范围获取报告列表
     */
    public List<ReportResponse> getReportsByDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("开始日期不能晚于结束日期");
        }

        List<Report> reports = reportRepository.findByStartDateGreaterThanEqualAndEndDateLessThanEqual(startDate, endDate);
        return reports.stream()
            .map(this::convertToReportResponse)
            .collect(Collectors.toList());
    }

    /**
     * 获取最新报告列表
     */
    public List<ReportResponse> getLatestReports(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        Page<Report> reports = reportRepository.findAll(
            (root, query, criteriaBuilder) -> {
                query.orderBy(criteriaBuilder.desc(root.get("createdAt")));
                return criteriaBuilder.conjunction();
            },
            pageable
        );
        return reports.getContent().stream()
            .map(this::convertToReportResponse)
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

    private ReportResponse convertToReportResponse(Report report) {
        return new ReportResponse(
            report.getId(),
            report.getTitle(),
            report.getReportType(),
            report.getStartDate(),
            report.getEndDate(),
            report.getContent(),
            report.getChartData(),
            report.getGeneratedBy(),
            report.getCreatedAt(),
            report.getUpdatedAt()
        );
    }
}
