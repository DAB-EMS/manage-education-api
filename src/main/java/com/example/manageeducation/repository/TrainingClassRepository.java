package com.example.manageeducation.repository;

import com.example.manageeducation.entity.TrainingClass;
import com.example.manageeducation.enums.TrainingClassStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TrainingClassRepository extends JpaRepository<TrainingClass, UUID> {
    List<TrainingClass> findAllByStatus(TrainingClassStatus status);
}
