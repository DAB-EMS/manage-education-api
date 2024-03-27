package com.example.manageeducation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.io.Serializable;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public class ProgramSyllabusId implements Serializable {
    @Column(name = "training_program_id")
    private UUID trainingProgramId;

    @Column(name = "syllabus_id")
    private UUID syllabusId;
}
