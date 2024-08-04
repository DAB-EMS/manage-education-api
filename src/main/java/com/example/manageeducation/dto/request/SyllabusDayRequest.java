package com.example.manageeducation.dto.request;

import com.example.manageeducation.entity.Syllabus;
import com.example.manageeducation.entity.SyllabusUnit;
import com.example.manageeducation.enums.SyllabusDayStatus;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SyllabusDayRequest {
    private int dayNo;

    private SyllabusDayStatus status;

    private List<SyllabusUnitRequest> syllabusUnits;
}
