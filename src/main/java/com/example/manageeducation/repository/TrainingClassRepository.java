package com.example.manageeducation.repository;

import com.example.manageeducation.entity.TrainingClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TrainingClassRepository extends JpaRepository<TrainingClass, UUID> {
}
