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
public class DeliveryPrinciple {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    private String trainees;
    private String trainer;
    @Column(length = 1337)
    private String training;
    private String re_test;
    private String marking;
    private String waiverCriteria;
    private String others;
    // syllabus_id
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "syllabus_id", referencedColumnName = "id")
    private Syllabus syllabus;
}
