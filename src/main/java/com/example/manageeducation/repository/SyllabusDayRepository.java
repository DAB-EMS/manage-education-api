package com.example.manageeducation.repository;

import com.example.manageeducation.entity.SyllabusDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SyllabusDayRepository extends JpaRepository<SyllabusDay, String> {

}
