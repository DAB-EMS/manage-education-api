package com.example.manageeducation.trainingprogramservice.jdbc;

import com.example.manageeducation.trainingprogramservice.mapper.TrainingProgramRowMapper;
import com.example.manageeducation.trainingprogramservice.model.TrainingProgram;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrainingProgramJdbc {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<TrainingProgram> getTrainingPrograms(String query){
        return jdbcTemplate.query(query, new TrainingProgramRowMapper());
    }

}
