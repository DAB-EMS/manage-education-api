package com.example.manageeducation.syllabusservice.dto.request;

import com.example.manageeducation.syllabusservice.enums.SyllabusStatus;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SyllabusRequest {
    private String name;
    private String code;
    private String version;
    private int attendeeNumber;
    private String technicalRequirement;
    private String courseObjective;
    @Enumerated(EnumType.STRING)
    private SyllabusStatus status;
    private boolean isTemplate;

    private AssessmentSchemeRequest assessmentScheme;

    private DeliveryPrincipleImportRequest deliveryPrinciple;

    private UUID syllabusLevel;

    private List<SyllabusDayRequest> syllabusDays;
}
