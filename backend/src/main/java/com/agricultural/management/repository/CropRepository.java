package com.agricultural.management.repository;

import com.agricultural.management.entity.Crop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CropRepository extends JpaRepository<Crop, Long>, JpaSpecificationExecutor<Crop> {

    List<Crop> findByPlantingSeason(String plantingSeason);

    List<Crop> findByGrowthPeriodGreaterThan(Integer days);

    List<Crop> findByGrowthPeriodLessThan(Integer days);

    boolean existsByNameAndVariety(String name, String variety);

    boolean existsByNameAndVarietyAndIdNot(String name, String variety, Long id);
}
