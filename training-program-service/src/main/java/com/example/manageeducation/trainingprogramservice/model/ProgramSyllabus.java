package com.example.manageeducation.trainingprogramservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@Entity
@Getter
@Setter
@ToString
public class ProgramSyllabus implements Serializable {

    @EmbeddedId
    private ProgramSyllabusId id;

    @ManyToOne
    @MapsId("trainingProgramId")
    @JoinColumn(name = "training_program_id")
    @JsonBackReference
    private TrainingProgram trainingProgram;

    @MapsId("syllabusId")
    @JoinColumn(name = "syllabus_id")
    @JsonBackReference
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID syllabus;
    private int position;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ProgramSyllabus that = (ProgramSyllabus) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
