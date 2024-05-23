package com.example.manageeducation.syllabusservice.repository;

import com.example.manageeducation.syllabusservice.enums.MaterialStatus;
import com.example.manageeducation.syllabusservice.model.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MaterialRepository extends JpaRepository<Material, UUID> {
    List<Material> findAllByUnitChapter_IdAndMaterialStatus(UUID id, MaterialStatus status);
}
