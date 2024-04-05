package com.example.manageeducation.repository;

import com.example.manageeducation.entity.ContactPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ContactPointRepository extends JpaRepository<ContactPoint, UUID> {
}
