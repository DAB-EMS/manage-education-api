package com.example.manageeducation.userservice.repository;

import com.example.manageeducation.userservice.enums.RoleType;
import com.example.manageeducation.userservice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(RoleType name);

}
