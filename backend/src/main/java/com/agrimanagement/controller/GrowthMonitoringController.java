package com.agrimanagement.controller;

import com.agrimanagement.entity.GrowthMonitoring;
import com.agrimanagement.service.impl.GrowthMonitoringServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/growth-monitoring")
@CrossOrigin(origins = "*")
public class GrowthMonitoringController {
    
    @Autowired
    private GrowthMonitoringServiceImpl growthMonitoringService;
    
    // 获取所有生长监控记录
    @GetMapping
    public ResponseEntity<List<GrowthMonitoring>> getAllGrowthMonitorings() {
        List<GrowthMonitoring> monitorings = growthMonitoringService.getAllGrowthMonitorings();
        return new ResponseEntity<>(monitorings, HttpStatus.OK);
    }
    
    // 根据ID获取生长监控记录
    @GetMapping("/{id}")
    public ResponseEntity<GrowthMonitoring> getGrowthMonitoringById(@PathVariable Long id) {
        Optional<GrowthMonitoring> monitoring = growthMonitoringService.getGrowthMonitoringById(id);
        if (monitoring.isPresent()) {
            return new ResponseEntity<>(monitoring.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // 创建新生长监控记录
    @PostMapping
    public ResponseEntity<GrowthMonitoring> createGrowthMonitoring(@RequestBody GrowthMonitoring growthMonitoring) {
        GrowthMonitoring createdMonitoring = growthMonitoringService.createGrowthMonitoring(growthMonitoring);
        return new ResponseEntity<>(createdMonitoring, HttpStatus.CREATED);
    }
    
    // 更新生长监控记录
    @PutMapping("/{id}")
    public ResponseEntity<GrowthMonitoring> updateGrowthMonitoring(@PathVariable Long id, @RequestBody GrowthMonitoring growthMonitoringDetails) {
        GrowthMonitoring updatedMonitoring = growthMonitoringService.updateGrowthMonitoring(id, growthMonitoringDetails);
        if (updatedMonitoring != null) {
            return new ResponseEntity<>(updatedMonitoring, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // 删除生长监控记录
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteGrowthMonitoring(@PathVariable Long id) {
        growthMonitoringService.deleteGrowthMonitoring(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}