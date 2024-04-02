package com.example.manageeducation.service;

import com.example.manageeducation.dto.request.MaterialRequest;
import com.example.manageeducation.dto.response.MaterialResponse;
import com.example.manageeducation.entity.Material;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface MaterialService {
    Material createMaterial(String customerId, UUID chapterId, MaterialRequest dto);
    Material updateMaterial(String customerId, UUID materialId, MaterialRequest dto);
    String deleteMaterial(UUID materialId);
    List<MaterialResponse> materials(UUID chapterId);

}
