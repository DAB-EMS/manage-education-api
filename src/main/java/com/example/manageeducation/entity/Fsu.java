package com.example.manageeducation.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Entity
@Getter
@Setter
public class Fsu {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private String name;

    @OneToMany(mappedBy = "fsu")
    @JsonIgnore
    private List<ContactPoint> contactPointList; // 1 - n to contactPoint

    @OneToMany(mappedBy = "fsu")
    @JsonIgnore
    private List<TrainingClass> trainingClassList;
}
