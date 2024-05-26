package com.example.manageeducation.trainingclassservice.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TrainingProgram {
    private UUID id;
    private String name;

    private boolean isTemplate;

    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID createdBy;

    private Date createdDate;

    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID updatedBy;

    private Date updatedDate;
    private String version;

}
