package com.example.manageeducation.trainingprogramservice.mapper;

import com.example.manageeducation.trainingprogramservice.enums.TrainingProgramStatus;
import com.example.manageeducation.trainingprogramservice.model.TrainingProgram;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class TrainingProgramRowMapper implements RowMapper<TrainingProgram> {

    @Override
    public TrainingProgram mapRow(ResultSet rs, int rowNum) throws SQLException {
        return TrainingProgram.builder()
                .id(UUID.fromString(rs.getString("id")))
                .name(rs.getString("name"))
                .createdDate(rs.getDate("created_date"))
                .createdBy(UUID.fromString(rs.getString("created_by")))
                .status(TrainingProgramStatus.valueOf(rs.getString("status")))
                .build();
    }
}
