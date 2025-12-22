package com.agrimanagement.service;

import com.agrimanagement.entity.Crop;
import java.util.List;
import java.util.Optional;

public interface CropService {
    List<Crop> getAllCrops();
    Optional<Crop> getCropById(Long id);
    Crop createCrop(Crop crop);
    Crop updateCrop(Long id, Crop cropDetails);
    void deleteCrop(Long id);
}