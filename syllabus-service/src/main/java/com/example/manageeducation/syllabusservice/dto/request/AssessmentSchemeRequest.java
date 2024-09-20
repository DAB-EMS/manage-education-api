package com.example.manageeducation.syllabusservice.dto.request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class AssessmentSchemeRequest {
    private Double assignment;
    private Double quiz;
    private Double exam;
    private Double gpa;
    private Double finalPoint;
    private Double finalTheory;
    private Double finalPractice;
}
