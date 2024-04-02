package com.example.manageeducation.repository;

import com.example.manageeducation.entity.SyllabusLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SyllabusLevelRepository extends JpaRepository<SyllabusLevel,String> {
}
