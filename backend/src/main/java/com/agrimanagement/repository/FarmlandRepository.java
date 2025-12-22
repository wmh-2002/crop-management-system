package com.agrimanagement.repository;

import com.agrimanagement.entity.Farmland;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FarmlandRepository extends JpaRepository<Farmland, Long> {
}