package com.example.manageeducation.syllabusservice.dto;

import com.example.manageeducation.syllabusservice.model.Syllabus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.UUID;

public class SyllabusRowMapper implements RowMapper<Syllabus> {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @Override
    public Syllabus mapRow(ResultSet rs, int rowNum) throws SQLException {
        try {
            return Syllabus.builder()
                    .id(UUID.fromString(rs.getString("id")))
                    .name(rs.getString("name"))
                    .code(rs.getString("code"))
                    .createdDate(dateFormat.parse(rs.getString("createdDate")))
                    .createdBy(UUID.fromString(rs.getString("createdBy")))
                    .build();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
