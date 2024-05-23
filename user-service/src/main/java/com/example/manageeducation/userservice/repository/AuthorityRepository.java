package com.example.manageeducation.userservice.repository;

import com.example.manageeducation.userservice.model.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, UUID> {
    List<Authority> findAllByResource(String resource);
    Optional<Authority> findOneById(UUID id);
}
