package com.example.manageeducation.trainingprogramservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SyllabusUnit {
    private UUID id;
    private String name;
    private int unitNo;
    private int duration;

    private List<SyllabusUnitChapter> syllabusUnitChapters;

    private Syllabus syllabus;

    private SyllabusDay syllabusDay;
}
