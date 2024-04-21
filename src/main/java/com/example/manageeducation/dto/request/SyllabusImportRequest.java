package com.example.manageeducation.dto.request;

import com.example.manageeducation.enums.SyllabusStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SyllabusImportRequest {
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
    private DeliveryPrincipleImportRequest deliveryPrinciple;

    private List<SyllabusDayImportRequest> syllabusDays;
    private List<OutputStandardRequest> standardRequests;
}
