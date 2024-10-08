package com.example.manageeducation.trainingclassservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Builder
public class Fsu {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;

    private String name;

    @OneToMany(mappedBy = "fsu")
    @JsonIgnore
    private List<ContactPoint> contactPointList; // 1 - n to contactPoint

    @OneToMany(mappedBy = "fsu")
    @JsonIgnore
    private List<TrainingClass> trainingClassList;
}
