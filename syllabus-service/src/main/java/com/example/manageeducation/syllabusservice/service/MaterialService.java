package com.example.manageeducation.syllabusservice.service;

import com.example.manageeducation.syllabusservice.dto.MaterialDTO;
import com.example.manageeducation.syllabusservice.dto.request.MaterialRequest;
import com.example.manageeducation.syllabusservice.dto.response.MaterialResponse;
import com.example.manageeducation.syllabusservice.model.Material;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Service
public interface MaterialService {
    Material createMaterial(Principal principal, UUID chapterId, MaterialRequest dto);
    Material updateMaterial(Principal principal, UUID materialId, MaterialRequest dto);
    String deleteMaterial(UUID materialId);
    List<MaterialResponse> materials(UUID chapterId);
    List<MaterialDTO> materialFull();

}
