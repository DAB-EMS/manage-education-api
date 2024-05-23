package com.example.manageeducation.trainingclassservice.repository;

import com.example.manageeducation.trainingclassservice.model.ClassLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClassLocationRepository extends JpaRepository<ClassLocation, UUID> {
    @Query("SELECT c from ClassLocation c where c.name=?1")
    ClassLocation findByName(String name);
}
