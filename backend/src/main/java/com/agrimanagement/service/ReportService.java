package com.agrimanagement.service;

import com.agrimanagement.entity.Report;
import java.util.List;
import java.util.Optional;

public interface ReportService {
    List<Report> getAllReports();
    Optional<Report> getReportById(Long id);
    Report createReport(Report report);
    Report updateReport(Long id, Report reportDetails);
    void deleteReport(Long id);
}