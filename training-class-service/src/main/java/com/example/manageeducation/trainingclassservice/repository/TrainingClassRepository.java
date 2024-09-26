package com.example.manageeducation.trainingclassservice.repository;

import com.example.manageeducation.trainingclassservice.dto.TrainingProgram;
import com.example.manageeducation.trainingclassservice.model.TrainingClass;
import com.example.manageeducation.trainingclassservice.enums.TrainingClassStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TrainingClassRepository extends JpaRepository<TrainingClass, UUID> {
    List<TrainingClass> findAllByStatus(TrainingClassStatus status);

    @Query("SELECT t from TrainingClass t where t.courseCode=?1")
    TrainingClass findByCourseCode(String courseCode);

    @Query("SELECT s FROM TrainingClass s WHERE s.status !=1")
    Page<TrainingClass> findAllTrainingClasses(Pageable pageable);

    @Query("SELECT COUNT(s) FROM TrainingClass s WHERE NOT s.status != 1")
    int getTotalRows();
}
