package com.example.manageeducation.entity;

import com.example.manageeducation.enums.TrainingProgramStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@Entity
@Getter
@Setter
public class TrainingProgram {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    @Column(columnDefinition = "varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL, FULLTEXT KEY name(name)")
    private String name;

    private boolean isTemplate;

    private UUID createdBy;

    private Date createdDate;

    private UUID updatedBy;

    private Date updatedDate;
    private String version;
    // private int duration; // day

    @Enumerated(EnumType.ORDINAL)
    private TrainingProgramStatus status;

    @OneToOne // đánh dấu có mối quan hệ 1-1 với Tranning class ở dưới
    @JoinColumn(name = "class_id", unique = true) // Liên kết với nhau qua khóa ngoại class_id
    @JsonIgnore
    private TrainingClass trainingClass;

    // Training Syllabus
    @OneToMany(mappedBy = "trainingProgram", cascade = CascadeType.ALL)
    @JsonIgnore
    @JsonManagedReference
    @ToString.Exclude
    @OrderBy(value = "position ASC")
    private List<ProgramSyllabus> programSyllabusAssociation; // relationship association

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
