package com.agrimanagement.controller;

import com.agrimanagement.entity.Crop;
import com.agrimanagement.service.impl.CropServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/crops")
@CrossOrigin(origins = "*")
public class CropController {
    
    @Autowired
    private CropServiceImpl cropService;
    
    // 获取所有作物
    @GetMapping
    public ResponseEntity<List<Crop>> getAllCrops() {
        List<Crop> crops = cropService.getAllCrops();
        return new ResponseEntity<>(crops, HttpStatus.OK);
    }
    
    // 根据ID获取作物
    @GetMapping("/{id}")
    public ResponseEntity<Crop> getCropById(@PathVariable Long id) {
        Optional<Crop> crop = cropService.getCropById(id);
        if (crop.isPresent()) {
            return new ResponseEntity<>(crop.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // 创建新作物
    @PostMapping
    public ResponseEntity<Crop> createCrop(@RequestBody Crop crop) {
        Crop createdCrop = cropService.createCrop(crop);
        return new ResponseEntity<>(createdCrop, HttpStatus.CREATED);
    }
    
    // 更新作物
    @PutMapping("/{id}")
    public ResponseEntity<Crop> updateCrop(@PathVariable Long id, @RequestBody Crop cropDetails) {
        Crop updatedCrop = cropService.updateCrop(id, cropDetails);
        if (updatedCrop != null) {
            return new ResponseEntity<>(updatedCrop, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // 删除作物
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteCrop(@PathVariable Long id) {
        cropService.deleteCrop(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}