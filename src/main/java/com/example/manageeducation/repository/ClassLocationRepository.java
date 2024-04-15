package com.example.manageeducation.repository;

import com.example.manageeducation.entity.ClassLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClassLocationRepository extends JpaRepository<ClassLocation, UUID> {
}
