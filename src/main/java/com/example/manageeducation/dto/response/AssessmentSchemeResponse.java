package com.example.manageeducation.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AssessmentSchemeResponse {
    private UUID id;
    private Double assignment;
    private Double quiz;
    private Double exam;
    private Double gpa;
    private Double finalPoint;
    private Double finalTheory;
    private Double finalPractice;
}
