package com.example.manageeducation.dto.request;

import com.example.manageeducation.entity.Syllabus;
import com.example.manageeducation.entity.SyllabusDay;
import com.example.manageeducation.entity.SyllabusUnitChapter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SyllabusUnitRequest {
    private String name;
    private int unitNo;
    private int duration;

    private List<SyllabusUnitChapterRequest> syllabusUnitChapters;
}
