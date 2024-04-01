package com.example.manageeducation.repository;

import com.example.manageeducation.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    Optional<Customer> findByEmail(String email);
}
