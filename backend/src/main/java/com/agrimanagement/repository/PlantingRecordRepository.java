package com.agrimanagement.repository;

import com.agrimanagement.entity.PlantingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantingRecordRepository extends JpaRepository<PlantingRecord, Long> {
}