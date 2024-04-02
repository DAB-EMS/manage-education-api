package com.example.manageeducation.repository;

import com.example.manageeducation.entity.Material;
import com.example.manageeducation.enums.MaterialStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MaterialRepository extends JpaRepository<Material, UUID> {
    List<Material> findAllByUnitChapter_IdAndMaterialStatus(UUID id, MaterialStatus status);
}
