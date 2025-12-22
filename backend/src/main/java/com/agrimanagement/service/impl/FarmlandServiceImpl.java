package com.agrimanagement.service.impl;

import com.agrimanagement.entity.Farmland;
import com.agrimanagement.repository.FarmlandRepository;
import com.agrimanagement.service.FarmlandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class FarmlandServiceImpl implements FarmlandService {
    
    @Autowired
    private FarmlandRepository farmlandRepository;
    
    @Override
    public List<Farmland> getAllFarmlands() {
        return farmlandRepository.findAll();
    }
    
    @Override
    public Optional<Farmland> getFarmlandById(Long id) {
        return farmlandRepository.findById(id);
    }
    
    @Override
    public Farmland createFarmland(Farmland farmland) {
        return farmlandRepository.save(farmland);
    }
    
    @Override
    public Farmland updateFarmland(Long id, Farmland farmlandDetails) {
        Farmland farmland = farmlandRepository.findById(id).orElse(null);
        if (farmland != null) {
            farmland.setName(farmlandDetails.getName());
            farmland.setArea(farmlandDetails.getArea());
            farmland.setLocation(farmlandDetails.getLocation());
            farmland.setStatus(farmlandDetails.getStatus());
            farmland.setDescription(farmlandDetails.getDescription());
            
            return farmlandRepository.save(farmland);
        }
        return null;
    }
    
    @Override
    public void deleteFarmland(Long id) {
        farmlandRepository.deleteById(id);
    }
}