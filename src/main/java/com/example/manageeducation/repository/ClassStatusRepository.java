package com.example.manageeducation.repository;

import com.example.manageeducation.entity.ClassStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClassStatusRepository extends JpaRepository<ClassStatus, UUID> {
}
