package com.example.manageeducation.syllabusservice.model;

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
public class SyllabusUnit {
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
    private int unitNo;
    private int duration;

    @OneToMany(mappedBy = "syllabusUnit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SyllabusUnitChapter> syllabusUnitChapters;

    // syllabus_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "syllabus_id")
    private Syllabus syllabus;

    // syllabus_day_id
//	@JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "syllabus_day_id")
    private SyllabusDay syllabusDay;
}
