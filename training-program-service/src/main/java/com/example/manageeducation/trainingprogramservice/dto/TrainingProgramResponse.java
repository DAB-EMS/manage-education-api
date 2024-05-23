package com.example.manageeducation.trainingprogramservice.dto;

import com.example.manageeducation.trainingprogramservice.enums.TrainingProgramStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TrainingProgramResponse {
    private UUID id;

    private String name;

    @Enumerated(EnumType.STRING)
    private TrainingProgramStatus status;

    private String createdBy;

    private Date createdDate;

    private int day;

    private int hours;

    private List<SyllabusResponse> syllabuses;
}
