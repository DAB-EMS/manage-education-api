package com.example.manageeducation.syllabusservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TimeAllocationResponse {
    private double concept;
    private double assignment;
    private double guides;
    private double test;
    private double exam;

}
