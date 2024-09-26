package com.example.manageeducation.trainingclassservice.mapper;

import com.example.manageeducation.trainingclassservice.model.AttendLevel;
import com.example.manageeducation.trainingclassservice.model.ClassLocation;
import com.example.manageeducation.trainingclassservice.model.Fsu;
import com.example.manageeducation.trainingclassservice.model.TrainingClass;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class TrainingClassRowMapper implements RowMapper<TrainingClass> {
    @Override
    public TrainingClass mapRow(ResultSet rs, int rowNum) throws SQLException {
        AttendLevel attendLevel = AttendLevel.builder()
                .id(UUID.fromString(rs.getString("attendeeLevelId")))
                .name(rs.getString("attendeeLevelName"))
                .build();
        ClassLocation classLocation = ClassLocation.builder()
                .id(UUID.fromString(rs.getString("classLocationId")))
                .name(rs.getString("classLocationName"))
                .build();
        Fsu fsu = Fsu.builder()
                .id(UUID.fromString(rs.getString("fsuId")))
                .name(rs.getString("fsuName"))
                .build();
        return TrainingClass.builder()
                .id(UUID.fromString(rs.getString("id")))
                .name(rs.getString("name"))
                .courseCode(rs.getString("courseCode"))
                .createdBy(UUID.fromString(rs.getString("createdBy")))
                .createdDate(LocalDateTime.parse(rs.getString("createdDate")))
                .duration(rs.getInt("duration"))
                .attendeeLevel(attendLevel)
                .classLocation(classLocation)
                .fsu(fsu)
                .build();
    }
}
