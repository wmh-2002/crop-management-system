package com.agrimanagement.service;

import com.agrimanagement.entity.Farmland;
import java.util.List;
import java.util.Optional;

public interface FarmlandService {
    List<Farmland> getAllFarmlands();
    Optional<Farmland> getFarmlandById(Long id);
    Farmland createFarmland(Farmland farmland);
    Farmland updateFarmland(Long id, Farmland farmlandDetails);
    void deleteFarmland(Long id);
}