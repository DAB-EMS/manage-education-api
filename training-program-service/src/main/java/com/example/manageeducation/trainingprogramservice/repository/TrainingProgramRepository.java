package com.example.manageeducation.trainingprogramservice.repository;

import com.example.manageeducation.trainingprogramservice.enums.TrainingProgramStatus;
import com.example.manageeducation.trainingprogramservice.model.TrainingProgram;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TrainingProgramRepository extends JpaRepository<TrainingProgram, UUID> {
    List<TrainingProgram> findAllByStatus(TrainingProgramStatus status);
    @Query("SELECT t from TrainingProgram t where t.name=?1 and t.version=?2")
    TrainingProgram findByName(String name, String version);
    @Query("SELECT tp FROM TrainingProgram tp WHERE tp.trainingClass IS NULL")
    List<TrainingProgram> findAllWithoutTrainingClass();

    @Query("SELECT s FROM TrainingProgram s WHERE NOT s.status =2")
    Page<TrainingProgram> findAllTrainingProgram(Pageable pageable);

    @Query("SELECT COUNT(s) FROM TrainingProgram s WHERE NOT s.status = 2")
    int getTotalRows();
}
