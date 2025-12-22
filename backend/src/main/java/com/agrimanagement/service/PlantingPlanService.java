package com.agrimanagement.service;

import com.agrimanagement.entity.PlantingPlan;
import java.util.List;
import java.util.Optional;

public interface PlantingPlanService {
    List<PlantingPlan> getAllPlantingPlans();
    Optional<PlantingPlan> getPlantingPlanById(Long id);
    PlantingPlan createPlantingPlan(PlantingPlan plantingPlan);
    PlantingPlan updatePlantingPlan(Long id, PlantingPlan plantingPlanDetails);
    void deletePlantingPlan(Long id);
}