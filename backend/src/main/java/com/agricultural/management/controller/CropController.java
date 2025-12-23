package com.agricultural.management.controller;

import com.agricultural.management.dto.*;
import com.agricultural.management.service.CropService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/crops")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CropController {

    private final CropService cropService;

    public CropController(CropService cropService) {
        this.cropService = cropService;
    }

    /**
     * 获取作物列表（分页）
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<PageResponse<CropResponse>>> getCropList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String variety,
            @RequestParam(required = false) String plantingSeason,
            @RequestParam(required = false) Integer minGrowthPeriod,
            @RequestParam(required = false) Integer maxGrowthPeriod) {

        try {
            CropQueryRequest request = new CropQueryRequest();
            request.setPage(page);
            request.setSize(size);
            request.setName(name);
            request.setVariety(variety);
            request.setPlantingSeason(plantingSeason);
            request.setMinGrowthPeriod(minGrowthPeriod);
            request.setMaxGrowthPeriod(maxGrowthPeriod);

            PageResponse<CropResponse> result = cropService.getCropList(request);
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            System.err.println("Error in getCropList: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<PageResponse<CropResponse>>error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 根据ID获取作物信息
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<CropResponse>> getCropById(@PathVariable Long id) {
        try {
            CropResponse crop = cropService.getCropById(id);
            return ResponseEntity.ok(ApiResponse.success(crop));
        } catch (Exception e) {
            System.err.println("Error in getCropById: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<CropResponse>error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 创建新作物
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CropResponse>> createCrop(@Valid @RequestBody CropCreateRequest request) {
        try {
            CropResponse newCrop = cropService.createCrop(request);
            return ResponseEntity.status(201).body(ApiResponse.success("作物创建成功", newCrop));
        } catch (Exception e) {
            System.err.println("Error in createCrop: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<CropResponse>error("创建作物失败: " + e.getMessage()));
        }
    }

    /**
     * 更新作物信息
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<CropResponse>> updateCrop(@PathVariable Long id, @Valid @RequestBody CropUpdateRequest request) {
        try {
            CropResponse updatedCrop = cropService.updateCrop(id, request);
            return ResponseEntity.ok(ApiResponse.success("作物更新成功", updatedCrop));
        } catch (Exception e) {
            System.err.println("Error in updateCrop: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<CropResponse>error("更新作物失败: " + e.getMessage()));
        }
    }

    /**
     * 删除作物
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<String>> deleteCrop(@PathVariable Long id) {
        try {
            cropService.deleteCrop(id);
            return ResponseEntity.ok(ApiResponse.success("作物删除成功"));
        } catch (Exception e) {
            System.err.println("Error in deleteCrop: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<String>error("删除作物失败: " + e.getMessage()));
        }
    }

    /**
     * 获取所有作物（不分页）
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<CropResponse>>> getAllCrops() {
        try {
            List<CropResponse> crops = cropService.getAllCrops();
            return ResponseEntity.ok(ApiResponse.success(crops));
        } catch (Exception e) {
            System.err.println("Error in getAllCrops: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<List<CropResponse>>error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 根据季节查询作物
     */
    @GetMapping("/season/{season}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<CropResponse>>> getCropsBySeason(@PathVariable String season) {
        try {
            List<CropResponse> crops = cropService.getCropsBySeason(season);
            return ResponseEntity.ok(ApiResponse.success(crops));
        } catch (Exception e) {
            System.err.println("Error in getCropsBySeason: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<List<CropResponse>>error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 根据生长期查询作物
     */
    @GetMapping("/growth-period")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<CropResponse>>> getCropsByGrowthPeriod(
            @RequestParam Integer minDays,
            @RequestParam(required = false) Integer maxDays) {
        try {
            List<CropResponse> crops = cropService.getCropsByGrowthPeriod(minDays, maxDays);
            return ResponseEntity.ok(ApiResponse.success(crops));
        } catch (Exception e) {
            System.err.println("Error in getCropsByGrowthPeriod: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<List<CropResponse>>error("查询失败: " + e.getMessage()));
        }
    }
}
