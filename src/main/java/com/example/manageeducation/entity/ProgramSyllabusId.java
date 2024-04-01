package com.example.manageeducation.entity;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProgramSyllabusId implements Serializable {
    @Column(name = "training_program_id")
    private UUID trainingProgramId;

    @Column(name = "syllabus_id")
    private UUID syllabusId;
}
