package com.example.manageeducation.repository;

import com.example.manageeducation.entity.Authority;
import com.example.manageeducation.entity.Role;
import com.example.manageeducation.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthorityRepository extends JpaRepository<Authority, UUID> {
    List<Authority> findAllByResource(String resource);
    Optional<Authority> findOneById(UUID id);
}
