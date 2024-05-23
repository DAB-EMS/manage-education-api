package com.example.manageeducation.trainingprogramservice.dto;

import com.example.manageeducation.trainingprogramservice.enums.SyllabusStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Syllabus {
    private UUID id;
    private String name;
    private String code;
    private String version;
    //	private String levelId; // fk
    private int attendeeNumber;

    private String technicalRequirement;

    private String courseObjective;
    //	private int deliveryPrincipleId;// fk
    private int days;
    private int hours;
    @Enumerated(EnumType.ORDINAL)
    private SyllabusStatus status;
    private boolean isTemplate;
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID createdBy;
    @CreationTimestamp
    private Date createdDate;
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID updatedBy;
    @CreationTimestamp
    private Date updatedDate;
    private List<SyllabusUnit> syllabusUnits;
}
