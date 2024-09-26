package com.example.manageeducation.trainingclassservice.dto.request;

import com.example.manageeducation.trainingclassservice.enums.TrainingClassStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestForListOfTrainingClass {
    private String[] keyword;
    private String[] location;
    private String[] attend;
    private String fsu;
    private String startDate;
    private String endDate;
    private TrainingClassStatus[] status;
    private int page;
    private int size;
    private String sortBy;
    private String sortType;
}
