package com.agrimanagement.service.impl;

import com.agrimanagement.entity.Report;
import com.agrimanagement.repository.ReportRepository;
import com.agrimanagement.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ReportServiceImpl implements ReportService {
    
    @Autowired
    private ReportRepository reportRepository;
    
    @Override
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }
    
    @Override
    public Optional<Report> getReportById(Long id) {
        return reportRepository.findById(id);
    }
    
    @Override
    public Report createReport(Report report) {
        return reportRepository.save(report);
    }
    
    @Override
    public Report updateReport(Long id, Report reportDetails) {
        Report report = reportRepository.findById(id).orElse(null);
        if (report != null) {
            report.setTitle(reportDetails.getTitle());
            report.setReportType(reportDetails.getReportType());
            report.setStartDate(reportDetails.getStartDate());
            report.setEndDate(reportDetails.getEndDate());
            report.setContent(reportDetails.getContent());
            report.setChartData(reportDetails.getChartData());
            
            return reportRepository.save(report);
        }
        return null;
    }
    
    @Override
    public void deleteReport(Long id) {
        reportRepository.deleteById(id);
    }
}