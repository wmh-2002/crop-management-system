package com.agrimanagement.service.impl;

import com.agrimanagement.entity.Crop;
import com.agrimanagement.repository.CropRepository;
import com.agrimanagement.service.CropService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CropServiceImpl implements CropService {
    
    @Autowired
    private CropRepository cropRepository;
    
    @Override
    public List<Crop> getAllCrops() {
        return cropRepository.findAll();
    }
    
    @Override
    public Optional<Crop> getCropById(Long id) {
        return cropRepository.findById(id);
    }
    
    @Override
    public Crop createCrop(Crop crop) {
        return cropRepository.save(crop);
    }
    
    @Override
    public Crop updateCrop(Long id, Crop cropDetails) {
        Crop crop = cropRepository.findById(id).orElse(null);
        if (crop != null) {
            crop.setName(cropDetails.getName());
            crop.setVariety(cropDetails.getVariety());
            crop.setPlantingSeason(cropDetails.getPlantingSeason());
            crop.setGrowthPeriod(cropDetails.getGrowthPeriod());
            crop.setExpectedYield(cropDetails.getExpectedYield());
            crop.setWaterNeeds(cropDetails.getWaterNeeds());
            crop.setFertilizerNeeds(cropDetails.getFertilizerNeeds());
            crop.setDiseaseInfo(cropDetails.getDiseaseInfo());
            crop.setDescription(cropDetails.getDescription());
            
            return cropRepository.save(crop);
        }
        return null;
    }
    
    @Override
    public void deleteCrop(Long id) {
        cropRepository.deleteById(id);
    }
}