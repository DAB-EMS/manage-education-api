package com.example.manageeducation.trainingclassservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SyllabusUnitResponse {
    private UUID id;
    private String name;
    private int unitNo;
    private int duration;
    private List<SyllabusUnitChapterResponse> syllabusUnitChapters;
}
