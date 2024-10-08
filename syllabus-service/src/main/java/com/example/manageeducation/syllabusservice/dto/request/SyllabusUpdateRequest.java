package com.example.manageeducation.syllabusservice.dto.request;

import com.example.manageeducation.syllabusservice.enums.SyllabusStatus;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SyllabusUpdateRequest {
    private String name;
    private String code;
    private String version;
    private int attendeeNumber;
    private String technicalRequirement;
    private String courseObjective;
    private int days;
    private int hours;
    @Enumerated(EnumType.STRING)
    private SyllabusStatus status;
    private boolean isTemplate;

    private AssessmentSchemeRequest assessmentScheme;

    private UUID syllabusLevel;
}
