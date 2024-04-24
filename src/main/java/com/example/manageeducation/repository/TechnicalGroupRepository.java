package com.example.manageeducation.repository;

import com.example.manageeducation.entity.TechnicalGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TechnicalGroupRepository extends JpaRepository<TechnicalGroup, UUID> {
    @Query("SELECT t from TechnicalGroup t where t.name=?1")
    TechnicalGroup findByName(String name);
}
