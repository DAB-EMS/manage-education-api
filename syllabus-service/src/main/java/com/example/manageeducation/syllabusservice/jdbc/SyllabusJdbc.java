package com.example.manageeducation.syllabusservice.jdbc;

import com.example.manageeducation.syllabusservice.dto.SyllabusRowMapper;
import com.example.manageeducation.syllabusservice.model.Syllabus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SyllabusJdbc {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public List<Syllabus> getSyllabus(String query) {
        return jdbcTemplate.query(query, new SyllabusRowMapper());
    }

    public int getTotalRows(String query){return jdbcTemplate.queryForObject(query, Integer.class);}
}
