package com.example.manageeducation.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;
@NoArgsConstructor
@Entity
@Getter
@Setter
public class ClassCalendar {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "class_id", nullable = true)
    private TrainingClass trainingClass;

    private int day_no;

    private LocalDateTime dateTime;

    private LocalTime beginTime;

    private LocalTime endTime;
}
