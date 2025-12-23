package com.agricultural.management.repository;

import com.agricultural.management.entity.Farmland;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FarmlandRepository extends JpaRepository<Farmland, Long>, JpaSpecificationExecutor<Farmland> {

    List<Farmland> findByCreatedBy(Long createdBy);

    List<Farmland> findByStatus(Farmland.FarmlandStatus status);

    boolean existsByNameAndCreatedBy(String name, Long createdBy);

    boolean existsByNameAndCreatedByAndIdNot(String name, Long createdBy, Long id);
}
