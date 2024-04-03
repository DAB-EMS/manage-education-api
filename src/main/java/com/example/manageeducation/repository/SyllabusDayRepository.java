package com.example.manageeducation.repository;

import com.example.manageeducation.entity.SyllabusDay;
import com.example.manageeducation.enums.SyllabusDayStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SyllabusDayRepository extends JpaRepository<SyllabusDay, UUID> {
    List<SyllabusDay> findAllBySyllabus_IdAndStatus(UUID id, SyllabusDayStatus status);
}
