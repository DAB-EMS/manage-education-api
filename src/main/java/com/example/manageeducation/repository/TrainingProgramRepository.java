package com.example.manageeducation.repository;

import com.example.manageeducation.entity.TrainingProgram;
import com.example.manageeducation.enums.TrainingProgramStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TrainingProgramRepository extends JpaRepository<TrainingProgram, UUID> {
    List<TrainingProgram> findAllByStatus(TrainingProgramStatus status);
    @Query("SELECT t from TrainingProgram t where t.name=?1 and t.version=?2")
    TrainingProgram findByName(String name, String version);
    @Query("SELECT tp FROM TrainingProgram tp WHERE tp.trainingClass IS NULL")
    List<TrainingProgram> findAllWithoutTrainingClass();
}
