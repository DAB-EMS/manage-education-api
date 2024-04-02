package com.example.manageeducation.dto.request;

import com.example.manageeducation.entity.*;
import com.example.manageeducation.enums.SyllabusStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SyllabusRequest {
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
    private UUID createdBy;
    @CreationTimestamp
    private Date createdDate;
    private UUID updatedBy;
    @CreationTimestamp
    private Date updatedDate;

    private AssessmentSchemeRequest assessmentScheme;

    private UUID syllabusLevel;

    private List<SyllabusDayRequest> syllabusDays;

//    private DeliveryPrinciple deliveryPrinciple;
//
//    private List<SyllabusUnit> syllabusUnits;
//
//    private List<ProgramSyllabus> programSyllabusAssociation;
}
