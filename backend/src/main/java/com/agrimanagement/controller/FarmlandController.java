package com.agrimanagement.controller;

import com.agrimanagement.entity.Farmland;
import com.agrimanagement.service.impl.FarmlandServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/farmlands")
@CrossOrigin(origins = "*")
public class FarmlandController {
    
    @Autowired
    private FarmlandServiceImpl farmlandService;
    
    // 获取所有农田
    @GetMapping
    public ResponseEntity<List<Farmland>> getAllFarmlands() {
        List<Farmland> farmlands = farmlandService.getAllFarmlands();
        return new ResponseEntity<>(farmlands, HttpStatus.OK);
    }
    
    // 根据ID获取农田
    @GetMapping("/{id}")
    public ResponseEntity<Farmland> getFarmlandById(@PathVariable Long id) {
        Optional<Farmland> farmland = farmlandService.getFarmlandById(id);
        if (farmland.isPresent()) {
            return new ResponseEntity<>(farmland.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // 创建新农田
    @PostMapping
    public ResponseEntity<Farmland> createFarmland(@RequestBody Farmland farmland) {
        Farmland createdFarmland = farmlandService.createFarmland(farmland);
        return new ResponseEntity<>(createdFarmland, HttpStatus.CREATED);
    }
    
    // 更新农田
    @PutMapping("/{id}")
    public ResponseEntity<Farmland> updateFarmland(@PathVariable Long id, @RequestBody Farmland farmlandDetails) {
        Farmland updatedFarmland = farmlandService.updateFarmland(id, farmlandDetails);
        if (updatedFarmland != null) {
            return new ResponseEntity<>(updatedFarmland, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // 删除农田
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteFarmland(@PathVariable Long id) {
        farmlandService.deleteFarmland(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}