package com.example.manageeducation.repository;

import com.example.manageeducation.entity.AttendLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AttendLevelRepository extends JpaRepository<AttendLevel, UUID> {
    @Query("SELECT a from AttendLevel a where a.name=?1")
    AttendLevel findByName(String name);
}
