package com.example.manageeducation.dto.response;

import com.example.manageeducation.entity.*;
import com.example.manageeducation.enums.SyllabusStatus;
import lombok.*;

import javax.persistence.*;
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

    private List<SyllabusDayViewResponse> syllabusDays;
}
