package com.agricultural.management.controller;

import com.agricultural.management.dto.*;
import com.agricultural.management.entity.Farmland;
import com.agricultural.management.service.FarmlandService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/farmlands")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FarmlandController {

    private final FarmlandService farmlandService;

    public FarmlandController(FarmlandService farmlandService) {
        this.farmlandService = farmlandService;
    }

    /**
     * 获取农田列表（分页）
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<PageResponse<FarmlandResponse>>> getFarmlandList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long createdBy) {

        try {
            FarmlandQueryRequest request = new FarmlandQueryRequest();
            request.setPage(page);
            request.setSize(size);
            request.setName(name);
            request.setLocation(location);

            // 转换状态字符串为枚举
            if (status != null && !status.trim().isEmpty()) {
                try {
                    request.setStatus(Farmland.FarmlandStatus.valueOf(status.toUpperCase()));
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid status: " + status);
                }
            }

            request.setCreatedBy(createdBy);

            PageResponse<FarmlandResponse> result = farmlandService.getFarmlandList(request);
            return ResponseEntity.ok(ApiResponse.success(result));
        } catch (Exception e) {
            System.err.println("Error in getFarmlandList: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<PageResponse<FarmlandResponse>>error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 根据ID获取农田信息
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<FarmlandResponse>> getFarmlandById(@PathVariable Long id) {
        try {
            FarmlandResponse farmland = farmlandService.getFarmlandById(id);
            return ResponseEntity.ok(ApiResponse.success(farmland));
        } catch (Exception e) {
            System.err.println("Error in getFarmlandById: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<FarmlandResponse>error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 创建新农田
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<FarmlandResponse>> createFarmland(@Valid @RequestBody FarmlandCreateRequest request) {
        try {
            FarmlandResponse newFarmland = farmlandService.createFarmland(request);
            return ResponseEntity.status(201).body(ApiResponse.success("农田创建成功", newFarmland));
        } catch (Exception e) {
            System.err.println("Error in createFarmland: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<FarmlandResponse>error("创建农田失败: " + e.getMessage()));
        }
    }

    /**
     * 更新农田信息
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<FarmlandResponse>> updateFarmland(@PathVariable Long id, @Valid @RequestBody FarmlandUpdateRequest request) {
        try {
            FarmlandResponse updatedFarmland = farmlandService.updateFarmland(id, request);
            return ResponseEntity.ok(ApiResponse.success("农田更新成功", updatedFarmland));
        } catch (Exception e) {
            System.err.println("Error in updateFarmland: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<FarmlandResponse>error("更新农田失败: " + e.getMessage()));
        }
    }

    /**
     * 删除农田
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<String>> deleteFarmland(@PathVariable Long id) {
        try {
            farmlandService.deleteFarmland(id);
            return ResponseEntity.ok(ApiResponse.success("农田删除成功"));
        } catch (Exception e) {
            System.err.println("Error in deleteFarmland: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<String>error("删除农田失败: " + e.getMessage()));
        }
    }

    /**
     * 获取当前用户的农田列表
     */
    @GetMapping("/my")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<FarmlandResponse>>> getMyFarmlands() {
        try {
            List<FarmlandResponse> farmlands = farmlandService.getMyFarmlands();
            return ResponseEntity.ok(ApiResponse.success(farmlands));
        } catch (Exception e) {
            System.err.println("Error in getMyFarmlands: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<List<FarmlandResponse>>error("查询失败: " + e.getMessage()));
        }
    }

    /**
     * 根据状态获取农田列表
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('FARMER') or hasRole('STAFF')")
    public ResponseEntity<ApiResponse<List<FarmlandResponse>>> getFarmlandsByStatus(@PathVariable String status) {
        try {
            Farmland.FarmlandStatus farmlandStatus = Farmland.FarmlandStatus.valueOf(status.toUpperCase());
            List<FarmlandResponse> farmlands = farmlandService.getFarmlandsByStatus(farmlandStatus);
            return ResponseEntity.ok(ApiResponse.success(farmlands));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.<List<FarmlandResponse>>error("无效的状态值: " + status));
        } catch (Exception e) {
            System.err.println("Error in getFarmlandsByStatus: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.<List<FarmlandResponse>>error("查询失败: " + e.getMessage()));
        }
    }
}
