package com.example.manageeducation.syllabusservice.model;

import com.example.manageeducation.syllabusservice.enums.SyllabusDayStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Entity
@Getter
@Setter
public class SyllabusDay {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;

    private int dayNo;

    @Enumerated(EnumType.ORDINAL)
    private SyllabusDayStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "syllabus_id")
    private Syllabus syllabus;

    @OneToMany(mappedBy = "syllabusDay", orphanRemoval = true)
    private List<SyllabusUnit> syllabusUnits;
}
