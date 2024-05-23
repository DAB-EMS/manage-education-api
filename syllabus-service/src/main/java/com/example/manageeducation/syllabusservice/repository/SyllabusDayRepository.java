package com.example.manageeducation.syllabusservice.repository;

import com.example.manageeducation.syllabusservice.enums.SyllabusDayStatus;
import com.example.manageeducation.syllabusservice.model.SyllabusDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SyllabusDayRepository extends JpaRepository<SyllabusDay, UUID> {
    List<SyllabusDay> findAllBySyllabus_IdAndStatus(UUID id, SyllabusDayStatus status);
}
