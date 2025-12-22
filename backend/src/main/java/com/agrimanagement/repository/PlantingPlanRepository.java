package com.agrimanagement.repository;

import com.agrimanagement.entity.PlantingPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantingPlanRepository extends JpaRepository<PlantingPlan, Long> {
}