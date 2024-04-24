package com.example.manageeducation.repository;

import com.example.manageeducation.entity.Fsu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FsuRepository extends JpaRepository<Fsu, UUID> {
    @Query("SELECT f from Fsu f where f.name=?1")
    Fsu findByName(String name);
}
