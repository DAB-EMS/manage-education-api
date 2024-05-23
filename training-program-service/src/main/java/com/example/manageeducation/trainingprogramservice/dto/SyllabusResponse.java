package com.example.manageeducation.trainingprogramservice.dto;

import com.example.manageeducation.trainingprogramservice.enums.SyllabusStatus;
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
public class SyllabusResponse {
    private UUID id;
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
    private String createdBy;
    private Date createdDate;
    private String updatedBy;
    private Date updatedDate;

    private AssessmentSchemeResponse assessmentScheme;

    private SyllabusLevelResponse syllabusLevel;

    private DeliveryPrincipleResponse deliveryPrinciple;

    private TimeAllocationResponse timeAllocationResponse;

    private List<SyllabusDayViewResponse> syllabusDays;
}
