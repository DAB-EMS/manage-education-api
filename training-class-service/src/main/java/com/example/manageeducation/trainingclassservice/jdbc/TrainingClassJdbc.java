package com.example.manageeducation.trainingclassservice.jdbc;


import com.example.manageeducation.trainingclassservice.mapper.TrainingClassRowMapper;
import com.example.manageeducation.trainingclassservice.model.TrainingClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrainingClassJdbc {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<TrainingClass> getTrainingClasses(String query){
        return jdbcTemplate.query(query, new TrainingClassRowMapper());
    }
}
