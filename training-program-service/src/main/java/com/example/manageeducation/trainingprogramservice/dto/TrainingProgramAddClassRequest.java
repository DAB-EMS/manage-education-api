package com.example.manageeducation.trainingprogramservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TrainingProgramAddClassRequest {
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;
    private String name;
    private String version;
    private String createdBy;
    private Date createdDate;
    private int hours;
    private int days;
}
