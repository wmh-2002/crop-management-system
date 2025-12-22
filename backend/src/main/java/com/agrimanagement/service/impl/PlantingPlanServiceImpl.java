package com.agrimanagement.service.impl;

import com.agrimanagement.entity.PlantingPlan;
import com.agrimanagement.repository.PlantingPlanRepository;
import com.agrimanagement.service.PlantingPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class PlantingPlanServiceImpl implements PlantingPlanService {
    
    @Autowired
    private PlantingPlanRepository plantingPlanRepository;
    
    @Override
    public List<PlantingPlan> getAllPlantingPlans() {
        return plantingPlanRepository.findAll();
    }
    
    @Override
    public Optional<PlantingPlan> getPlantingPlanById(Long id) {
        return plantingPlanRepository.findById(id);
    }
    
    @Override
    public PlantingPlan createPlantingPlan(PlantingPlan plantingPlan) {
        return plantingPlanRepository.save(plantingPlan);
    }
    
    @Override
    public PlantingPlan updatePlantingPlan(Long id, PlantingPlan plantingPlanDetails) {
        PlantingPlan plan = plantingPlanRepository.findById(id).orElse(null);
        if (plan != null) {
            plan.setPlanName(plantingPlanDetails.getPlanName());
            plan.setPlannedStartDate(plantingPlanDetails.getPlannedStartDate());
            plan.setPlannedEndDate(plantingPlanDetails.getPlannedEndDate());
            plan.setExpectedHarvestDate(plantingPlanDetails.getExpectedHarvestDate());
            plan.setSowingDensity(plantingPlanDetails.getSowingDensity());
            plan.setNotes(plantingPlanDetails.getNotes());
            plan.setStatus(plantingPlanDetails.getStatus());
            
            return plantingPlanRepository.save(plan);
        }
        return null;
    }
    
    @Override
    public void deletePlantingPlan(Long id) {
        plantingPlanRepository.deleteById(id);
    }
}