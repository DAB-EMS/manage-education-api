package com.example.manageeducation.userservice.mapper;

import com.example.manageeducation.userservice.enums.CustomerStatus;
import com.example.manageeducation.userservice.enums.Gender;
import com.example.manageeducation.userservice.model.Customer;
import com.example.manageeducation.userservice.model.Role;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.UUID;

public class CustomerRowMapper implements RowMapper<Customer> {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @Override
    public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
        try {
            return Customer.builder()
                    .id(UUID.fromString(rs.getString("id")))
                    .fullName(rs.getString("fullName"))
                    .email(rs.getString("email"))
                    .avatar(rs.getString("avatar"))
                    .expiredDate(Instant.parse(rs.getString("expiredDate")))
                    .createdDate(Instant.parse(rs.getString("createdDate")))
                    .updatedDate(Instant.parse(rs.getString("updatedDate")))
                    .gender(Gender.valueOf(rs.getString("gender")))
                    .level(rs.getString("level"))
                    .status(CustomerStatus.valueOf(rs.getString("status")))
                    .birthday(dateFormat.parse(rs.getString("birthday")))
                    .build();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
