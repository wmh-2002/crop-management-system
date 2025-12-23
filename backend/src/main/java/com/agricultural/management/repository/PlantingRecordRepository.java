package com.agricultural.management.repository;

import com.agricultural.management.entity.PlantingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PlantingRecordRepository extends JpaRepository<PlantingRecord, Long>, JpaSpecificationExecutor<PlantingRecord> {

    List<PlantingRecord> findByPlanId(Long planId);

    List<PlantingRecord> findByOperationType(PlantingRecord.OperationType operationType);

    List<PlantingRecord> findByOperatorId(Long operatorId);

    List<PlantingRecord> findByPlanIdAndOperationType(Long planId, PlantingRecord.OperationType operationType);

    List<PlantingRecord> findByOperationDateBetween(LocalDate startDate, LocalDate endDate);

    List<PlantingRecord> findByPlanIdAndOperationDateBetween(Long planId, LocalDate startDate, LocalDate endDate);

    List<PlantingRecord> findByOperatorIdAndOperationDateBetween(Long operatorId, LocalDate startDate, LocalDate endDate);

    boolean existsByPlanIdAndOperationDateAndOperationType(Long planId, LocalDate operationDate, PlantingRecord.OperationType operationType);
}
