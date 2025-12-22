package com.agrimanagement.controller;

import com.agrimanagement.entity.PlantingPlan;
import com.agrimanagement.service.impl.PlantingPlanServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/planting-plans")
@CrossOrigin(origins = "*")
public class PlantingPlanController {
    
    @Autowired
    private PlantingPlanServiceImpl plantingPlanService;
    
    // 获取所有种植计划
    @GetMapping
    public ResponseEntity<List<PlantingPlan>> getAllPlantingPlans() {
        List<PlantingPlan> plans = plantingPlanService.getAllPlantingPlans();
        return new ResponseEntity<>(plans, HttpStatus.OK);
    }
    
    // 根据ID获取种植计划
    @GetMapping("/{id}")
    public ResponseEntity<PlantingPlan> getPlantingPlanById(@PathVariable Long id) {
        Optional<PlantingPlan> plan = plantingPlanService.getPlantingPlanById(id);
        if (plan.isPresent()) {
            return new ResponseEntity<>(plan.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // 创建新种植计划
    @PostMapping
    public ResponseEntity<PlantingPlan> createPlantingPlan(@RequestBody PlantingPlan plantingPlan) {
        PlantingPlan createdPlan = plantingPlanService.createPlantingPlan(plantingPlan);
        return new ResponseEntity<>(createdPlan, HttpStatus.CREATED);
    }
    
    // 更新种植计划
    @PutMapping("/{id}")
    public ResponseEntity<PlantingPlan> updatePlantingPlan(@PathVariable Long id, @RequestBody PlantingPlan plantingPlanDetails) {
        PlantingPlan updatedPlan = plantingPlanService.updatePlantingPlan(id, plantingPlanDetails);
        if (updatedPlan != null) {
            return new ResponseEntity<>(updatedPlan, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // 删除种植计划
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deletePlantingPlan(@PathVariable Long id) {
        plantingPlanService.deletePlantingPlan(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}