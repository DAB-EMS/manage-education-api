package com.example.manageeducation.repository;

import com.example.manageeducation.entity.Syllabus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface SyllabusRepository extends JpaRepository<Syllabus, UUID> {
    List<Syllabus> findAllByNameAndCodeAndCreatedByAndCreatedDate(String name, String code, String by, Date createdDate);
}
