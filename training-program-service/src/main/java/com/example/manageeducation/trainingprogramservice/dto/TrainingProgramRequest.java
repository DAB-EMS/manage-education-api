package com.example.manageeducation.trainingprogramservice.dto;

import com.example.manageeducation.trainingprogramservice.enums.TrainingProgramStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TrainingProgramRequest {
    private String name;

    private boolean isTemplate;

    private String version;

    private TrainingProgramStatus status;

    private List<ProgramSyllabusRequest> programSyllabuses;
}
