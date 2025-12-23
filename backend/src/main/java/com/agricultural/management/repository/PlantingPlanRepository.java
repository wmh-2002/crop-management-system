package com.agricultural.management.repository;

import com.agricultural.management.entity.PlantingPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PlantingPlanRepository extends JpaRepository<PlantingPlan, Long>, JpaSpecificationExecutor<PlantingPlan> {

    List<PlantingPlan> findByFarmlandId(Long farmlandId);

    List<PlantingPlan> findByCropId(Long cropId);

    List<PlantingPlan> findByStatus(PlantingPlan.PlantingStatus status);

    List<PlantingPlan> findByCreatedBy(Long createdBy);

    List<PlantingPlan> findByPlannedStartDateBetween(LocalDate startDate, LocalDate endDate);

    List<PlantingPlan> findByPlannedEndDateBetween(LocalDate startDate, LocalDate endDate);

    List<PlantingPlan> findByExpectedHarvestDateBetween(LocalDate startDate, LocalDate endDate);

    boolean existsByFarmlandIdAndPlannedStartDateLessThanEqualAndPlannedEndDateGreaterThanEqual(
        Long farmlandId, LocalDate endDate, LocalDate startDate);

    boolean existsByFarmlandIdAndCropIdAndPlannedStartDateLessThanEqualAndPlannedEndDateGreaterThanEqual(
        Long farmlandId, Long cropId, LocalDate endDate, LocalDate startDate);
}
