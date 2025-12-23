package com.agricultural.management.service;

import com.agricultural.management.dto.*;
import com.agricultural.management.entity.Crop;
import com.agricultural.management.exception.DuplicateEntryException;
import com.agricultural.management.exception.ResourceNotFoundException;
import com.agricultural.management.repository.CropRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CropService {

    private final CropRepository cropRepository;

    public CropService(CropRepository cropRepository) {
        this.cropRepository = cropRepository;
    }

    /**
     * 分页查询作物列表
     */
    public PageResponse<CropResponse> getCropList(CropQueryRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());

        Specification<Crop> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getName() != null && !request.getName().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("name"),
                    "%" + request.getName().trim() + "%"));
            }

            if (request.getVariety() != null && !request.getVariety().trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("variety"),
                    "%" + request.getVariety().trim() + "%"));
            }

            if (request.getPlantingSeason() != null && !request.getPlantingSeason().trim().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("plantingSeason"), request.getPlantingSeason()));
            }

            if (request.getMinGrowthPeriod() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("growthPeriod"), request.getMinGrowthPeriod()));
            }

            if (request.getMaxGrowthPeriod() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("growthPeriod"), request.getMaxGrowthPeriod()));
            }

            if (predicates.isEmpty()) {
                return null; // 没有查询条件，返回所有数据
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Page<Crop> cropPage = cropRepository.findAll(spec, pageable);

        List<CropResponse> cropResponses = cropPage.getContent().stream()
            .map(this::convertToCropResponse)
            .collect(Collectors.toList());

        return PageResponse.<CropResponse>builder()
            .content(cropResponses)
            .page(cropPage.getNumber() + 1)
            .size(cropPage.getSize())
            .totalElements(cropPage.getTotalElements())
            .totalPages(cropPage.getTotalPages())
            .last(cropPage.isLast())
            .build();
    }

    /**
     * 根据ID获取作物信息
     */
    public CropResponse getCropById(Long id) {
        Crop crop = cropRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("作物不存在，ID: " + id));
        return convertToCropResponse(crop);
    }

    /**
     * 创建新作物
     */
    @Transactional
    public CropResponse createCrop(CropCreateRequest request) {
        // 检查作物名称和品种的唯一性
        if (cropRepository.existsByNameAndVariety(request.getName(), request.getVariety())) {
            throw new DuplicateEntryException("作物已存在: " + request.getName() +
                (request.getVariety() != null ? " (" + request.getVariety() + ")" : ""));
        }

        Crop crop = new Crop();
        crop.setName(request.getName());
        crop.setVariety(request.getVariety());
        crop.setPlantingSeason(request.getPlantingSeason());
        crop.setGrowthPeriod(request.getGrowthPeriod());
        crop.setExpectedYield(request.getExpectedYield());
        crop.setWaterNeeds(request.getWaterNeeds());
        crop.setFertilizerNeeds(request.getFertilizerNeeds());
        crop.setDiseaseInfo(request.getDiseaseInfo());
        crop.setDescription(request.getDescription());

        Crop savedCrop = cropRepository.save(crop);
        return convertToCropResponse(savedCrop);
    }

    /**
     * 更新作物信息
     */
    @Transactional
    public CropResponse updateCrop(Long id, CropUpdateRequest request) {
        Crop crop = cropRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("作物不存在，ID: " + id));

        // 检查更新后的名称和品种是否与其他作物重复
        String newName = request.getName() != null ? request.getName() : crop.getName();
        String newVariety = request.getVariety() != null ? request.getVariety() : crop.getVariety();

        if (!newName.equals(crop.getName()) || !java.util.Objects.equals(newVariety, crop.getVariety())) {
            if (cropRepository.existsByNameAndVarietyAndIdNot(newName, newVariety, id)) {
                throw new DuplicateEntryException("作物已存在: " + newName +
                    (newVariety != null ? " (" + newVariety + ")" : ""));
            }
        }

        if (request.getName() != null) {
            crop.setName(request.getName());
        }
        if (request.getVariety() != null) {
            crop.setVariety(request.getVariety());
        }
        if (request.getPlantingSeason() != null) {
            crop.setPlantingSeason(request.getPlantingSeason());
        }
        if (request.getGrowthPeriod() != null) {
            crop.setGrowthPeriod(request.getGrowthPeriod());
        }
        if (request.getExpectedYield() != null) {
            crop.setExpectedYield(request.getExpectedYield());
        }
        if (request.getWaterNeeds() != null) {
            crop.setWaterNeeds(request.getWaterNeeds());
        }
        if (request.getFertilizerNeeds() != null) {
            crop.setFertilizerNeeds(request.getFertilizerNeeds());
        }
        if (request.getDiseaseInfo() != null) {
            crop.setDiseaseInfo(request.getDiseaseInfo());
        }
        if (request.getDescription() != null) {
            crop.setDescription(request.getDescription());
        }

        Crop updatedCrop = cropRepository.save(crop);
        return convertToCropResponse(updatedCrop);
    }

    /**
     * 删除作物
     */
    @Transactional
    public void deleteCrop(Long id) {
        if (!cropRepository.existsById(id)) {
            throw new ResourceNotFoundException("作物不存在，ID: " + id);
        }

        cropRepository.deleteById(id);
    }

    /**
     * 获取指定季节的作物
     */
    public List<CropResponse> getCropsBySeason(String plantingSeason) {
        List<Crop> crops = cropRepository.findByPlantingSeason(plantingSeason);
        return crops.stream()
            .map(this::convertToCropResponse)
            .collect(Collectors.toList());
    }

    /**
     * 获取生长期在指定范围内的作物
     */
    public List<CropResponse> getCropsByGrowthPeriod(Integer minDays, Integer maxDays) {
        List<Crop> crops = cropRepository.findByGrowthPeriodGreaterThan(minDays - 1);
        if (maxDays != null) {
            crops.retainAll(cropRepository.findByGrowthPeriodLessThan(maxDays + 1));
        }
        return crops.stream()
            .map(this::convertToCropResponse)
            .collect(Collectors.toList());
    }

    /**
     * 获取所有作物（不分页）
     */
    public List<CropResponse> getAllCrops() {
        List<Crop> crops = cropRepository.findAll();
        return crops.stream()
            .map(this::convertToCropResponse)
            .collect(Collectors.toList());
    }

    private CropResponse convertToCropResponse(Crop crop) {
        return new CropResponse(
            crop.getId(),
            crop.getName(),
            crop.getVariety(),
            crop.getPlantingSeason(),
            crop.getGrowthPeriod(),
            crop.getExpectedYield(),
            crop.getWaterNeeds(),
            crop.getFertilizerNeeds(),
            crop.getDiseaseInfo(),
            crop.getDescription(),
            crop.getCreatedAt(),
            crop.getUpdatedAt()
        );
    }
}
