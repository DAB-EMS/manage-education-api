package com.example.manageeducation.repository;

import com.example.manageeducation.entity.Customer;
import com.example.manageeducation.enums.CustomerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    Optional<Customer> findByEmail(String email);
    @Query("SELECT u from Customer u where u.email=?1")
    Customer findCustomerByEmail(String email);
    List<Customer> findByFullNameContainingIgnoreCase(String name);
    List<Customer> findAllByStatus(CustomerStatus status);
}
