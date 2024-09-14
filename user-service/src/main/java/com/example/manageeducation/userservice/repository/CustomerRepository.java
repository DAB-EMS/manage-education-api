package com.example.manageeducation.userservice.repository;

import com.example.manageeducation.userservice.enums.CustomerStatus;
import com.example.manageeducation.userservice.enums.RoleType;
import com.example.manageeducation.userservice.model.Customer;
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
    @Query("SELECT COUNT(s) FROM Customer s WHERE s.status = 1")
    int getTotalRows();
    List<Customer> findByFullNameContainingIgnoreCase(String name);
    List<Customer> findAllByStatus(CustomerStatus status);
    List<Customer> findAllByRole_Name(RoleType role);
}
