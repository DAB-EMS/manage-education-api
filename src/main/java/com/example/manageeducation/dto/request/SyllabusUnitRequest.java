package com.example.manageeducation.dto.request;

import com.example.manageeducation.entity.Syllabus;
import com.example.manageeducation.entity.SyllabusDay;
import com.example.manageeducation.entity.SyllabusUnitChapter;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SyllabusUnitRequest {
    private String name;
    private int unitNo;

    private List<SyllabusUnitChapterRequest> syllabusUnitChapters;
}
