package com.example.manageeducation.trainingprogramservice.model;

import com.example.manageeducation.trainingprogramservice.enums.TrainingProgramStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@ToString
@Builder
public class TrainingProgram {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
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

    @Enumerated(EnumType.ORDINAL)
    private TrainingProgramStatus status;

    @JoinColumn(name = "class_id", unique = true)
    @JsonIgnore
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID trainingClass;

    @OneToMany(mappedBy = "trainingProgram", cascade = CascadeType.ALL)
    @JsonIgnore
    @JsonManagedReference
    @ToString.Exclude
    @OrderBy(value = "position ASC")
    private List<ProgramSyllabus> programSyllabusAssociation;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TrainingProgram that = (TrainingProgram) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
