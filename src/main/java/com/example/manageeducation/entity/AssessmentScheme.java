package com.example.manageeducation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import java.util.UUID;

@NoArgsConstructor
@Entity
@Getter
@Setter
public class AssessmentScheme {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    private Double assignment;
    private Double quiz;
    private Double exam;
    private Double gpa;
    private Double finalPoint;
    private Double finalTheory;
    private Double finalPractice;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "syllabus_id", referencedColumnName = "id")
    private Syllabus syllabus;
}
