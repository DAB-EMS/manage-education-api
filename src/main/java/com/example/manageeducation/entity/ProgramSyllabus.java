package com.example.manageeducation.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    @MapsId("training_program_id")
    @JoinColumn(name = "training_program_id")
    @JsonBackReference
    private TrainingProgram trainingProgram;

    @ManyToOne
    @MapsId("syllabus_id")
    @JoinColumn(name = "syllabus_id")
    @JsonBackReference
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
