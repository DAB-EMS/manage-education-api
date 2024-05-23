package com.example.manageeducation.trainingclassservice.repository;

import com.example.manageeducation.trainingclassservice.model.TrainingClass;
import com.example.manageeducation.trainingclassservice.enums.TrainingClassStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TrainingClassRepository extends JpaRepository<TrainingClass, UUID> {
    List<TrainingClass> findAllByStatus(TrainingClassStatus status);
    @Query(value = "SELECT t from TrainingClass t where t.trainingProgram=?1")
    TrainingClass findClassByTrainingProgramId(TrainingProgram trainingProgramId);
    @Query("SELECT t from TrainingClass t where t.courseCode=?1")
    TrainingClass findByCourseCode(String courseCode);
}
