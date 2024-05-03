package com.example.manageeducation.repository;

import com.example.manageeducation.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthorityRepository extends JpaRepository<Authority, UUID> {
    List<Authority> findAllByResource(String resource);
    Optional<Authority> findOneById(UUID id);
}
