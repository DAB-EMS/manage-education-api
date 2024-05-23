package com.example.manageeducation.trainingprogramservice.dto;

import com.example.manageeducation.trainingprogramservice.enums.SyllabusDayStatus;
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
public class SyllabusDay {
    private UUID id;

    private int dayNo;

    @Enumerated(EnumType.ORDINAL)
    private SyllabusDayStatus status;

    private Syllabus syllabus;

    private List<SyllabusUnit> syllabusUnits;
}
