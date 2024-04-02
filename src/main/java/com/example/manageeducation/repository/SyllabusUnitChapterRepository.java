package com.example.manageeducation.repository;

import com.example.manageeducation.entity.SyllabusUnitChapter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SyllabusUnitChapterRepository extends JpaRepository<SyllabusUnitChapter, UUID> {
}
