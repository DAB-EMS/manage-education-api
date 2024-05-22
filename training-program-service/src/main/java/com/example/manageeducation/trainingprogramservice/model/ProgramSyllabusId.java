package com.example.manageeducation.trainingprogramservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProgramSyllabusId implements Serializable {
    @Column(name = "training_program_id")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID trainingProgramId;

    @Column(name = "syllabus_id")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID syllabusId;
}
