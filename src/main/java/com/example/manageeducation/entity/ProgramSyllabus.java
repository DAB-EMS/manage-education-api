package com.example.manageeducation.entity;

import javax.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@Entity
@Getter
@Setter
public class ProgramSyllabus implements Serializable {

    @EmbeddedId
    private ProgramSyllabusId id;

    @ManyToOne
    @MapsId("trainingProgramId")
    @JoinColumn(name = "training_program_id", insertable = false, updatable = false)
    private TrainingProgram trainingProgram;

    @ManyToOne
    @MapsId("syllabusId")
    @JoinColumn(name = "syllabus_id", insertable = false, updatable = false)
    private Syllabus syllabus;
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
