package com.example.manageeducation.trainingprogramservice.dto;

import com.example.manageeducation.trainingprogramservice.enums.TrainingProgramStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestForListOfTrainingProgram {
    private String[] keyword;
    private String startDate;
    private String endDate;
    private TrainingProgramStatus status;
    private int page;
    private int size;
    private String sortBy;
    private String sortType;
}
