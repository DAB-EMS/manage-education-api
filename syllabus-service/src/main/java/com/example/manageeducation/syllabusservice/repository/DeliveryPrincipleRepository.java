package com.example.manageeducation.syllabusservice.repository;

import com.example.manageeducation.syllabusservice.model.DeliveryPrinciple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface DeliveryPrincipleRepository extends JpaRepository<DeliveryPrinciple, UUID> {
}
