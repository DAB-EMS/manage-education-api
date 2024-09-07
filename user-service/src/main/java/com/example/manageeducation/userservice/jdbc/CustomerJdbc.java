package com.example.manageeducation.userservice.jdbc;

import com.example.manageeducation.userservice.mapper.CustomerRowMapper;
import com.example.manageeducation.userservice.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomerJdbc {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Customer> getCustomers(String query) {
        return jdbcTemplate.query(query, new CustomerRowMapper());
    }

    public int getTotalRows(String query){return jdbcTemplate.queryForObject(query, Integer.class);}
}
