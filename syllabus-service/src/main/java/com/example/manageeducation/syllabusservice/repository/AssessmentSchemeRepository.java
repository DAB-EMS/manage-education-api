package com.example.manageeducation.syllabusservice.repository;

import com.example.manageeducation.syllabusservice.model.AssessmentScheme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AssessmentSchemeRepository extends JpaRepository<AssessmentScheme, UUID> {
}
