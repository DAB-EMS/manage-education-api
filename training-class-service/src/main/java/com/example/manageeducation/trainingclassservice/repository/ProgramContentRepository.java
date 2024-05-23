package com.example.manageeducation.trainingclassservice.repository;

import com.example.manageeducation.trainingclassservice.model.ProgramContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProgramContentRepository extends JpaRepository<ProgramContent, UUID> {
    @Query("SELECT p from ProgramContent p where p.name=?1")
    ProgramContent findByName(String name);
}
