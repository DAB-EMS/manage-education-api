package com.example.manageeducation.trainingclassservice.repository;

import com.example.manageeducation.trainingclassservice.model.ClassCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClassCalendarRepository extends JpaRepository<ClassCalendar, UUID> {
}
