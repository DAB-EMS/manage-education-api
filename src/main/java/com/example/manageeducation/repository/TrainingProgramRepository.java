package com.example.manageeducation.repository;

import com.example.manageeducation.entity.TrainingProgram;
import com.example.manageeducation.enums.TrainingProgramStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TrainingProgramRepository extends JpaRepository<TrainingProgram, UUID> {
    List<TrainingProgram> findAllByStatus(TrainingProgramStatus status);
}
