package com.example.manageeducation.syllabusservice.repository;

import com.example.manageeducation.syllabusservice.model.DeliveryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeliveryTypeRepository extends JpaRepository<DeliveryType, UUID> {
    Optional<DeliveryType> findByNameIgnoreCase(String name);
}
