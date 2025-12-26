package com.agricultural.management.repository;

import com.agricultural.management.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long>, JpaSpecificationExecutor<Report> {

    List<Report> findByReportType(Report.ReportType reportType);

    List<Report> findByGeneratedBy(Long generatedBy);

    List<Report> findByStartDateBetween(LocalDate startDate, LocalDate endDate);

    List<Report> findByEndDateBetween(LocalDate startDate, LocalDate endDate);

    List<Report> findByReportTypeAndGeneratedBy(Report.ReportType reportType, Long generatedBy);

    List<Report> findByStartDateGreaterThanEqualAndEndDateLessThanEqual(LocalDate startDate, LocalDate endDate);

    List<Report> findByReportTypeOrderByCreatedAtDesc(Report.ReportType reportType);

    List<Report> findByGeneratedByOrderByCreatedAtDesc(Long generatedBy);

    List<Report> findByCreatedAtBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
