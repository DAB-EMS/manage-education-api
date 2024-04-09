package com.example.manageeducation.repository;

import com.example.manageeducation.entity.Syllabus;
import com.example.manageeducation.enums.SyllabusStatus;
import com.example.manageeducation.enums.TrainingProgramStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SyllabusRepository extends JpaRepository<Syllabus, UUID> {
    List<Syllabus> findAllByNameAndCodeAndCreatedByAndCreatedDateAndStatus(String name, String code, String by, Date createdDate, SyllabusStatus status);
    List<Syllabus> findAllByStatus(SyllabusStatus status);
    Optional<Syllabus> findByIdAndStatus(UUID id, SyllabusStatus status);
    List<Syllabus> findByNameIgnoreCaseAndCodeIgnoreCaseAndVersionIgnoreCaseAndStatus(String name, String code, String version, SyllabusStatus status);
}
