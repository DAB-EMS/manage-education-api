package com.example.manageeducation.syllabusservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SyllabusUnitRequest {
    private String name;
    private int unitNo;

    private List<SyllabusUnitChapterRequest> syllabusUnitChapters;
}
