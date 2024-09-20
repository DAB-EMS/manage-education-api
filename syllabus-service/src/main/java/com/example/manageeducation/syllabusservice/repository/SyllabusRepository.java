package com.example.manageeducation.syllabusservice.repository;

import com.example.manageeducation.syllabusservice.enums.SyllabusStatus;
import com.example.manageeducation.syllabusservice.model.Syllabus;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SyllabusRepository extends JpaRepository<Syllabus, UUID> {
    List<Syllabus> findAllByStatus(SyllabusStatus status);
    Optional<Syllabus> findByIdAndStatus(UUID id, SyllabusStatus status);
    List<Syllabus> findByNameIgnoreCaseAndCodeIgnoreCaseAndVersionIgnoreCaseAndStatus(String name, String code, String version, SyllabusStatus status);

    @Query("SELECT SUM(suc.duration) FROM SyllabusUnitChapter suc " +
            "INNER JOIN suc.syllabusUnit su " +
            "WHERE su.syllabus.id = :syllabusId AND suc.deliveryType.id = :deliveryId")
    Double sumDurationBySyllabusIdAndDeliveryId(UUID syllabusId, UUID deliveryId);

    @Query("SELECT s FROM Syllabus s WHERE NOT s.status = 1 AND NOT s.status = 2 AND NOT s.status = 3 AND NOT s.status = 4")
    Page<Syllabus> findAllSyllabus(Pageable pageable);

    @Query("SELECT COUNT(s) FROM Syllabus s WHERE s.status = 0")
    int getTotalRows();

    Syllabus findSyllabusById(UUID id);
}
