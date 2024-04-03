package com.example.manageeducation.dto.response;

import com.example.manageeducation.entity.Syllabus;
import com.example.manageeducation.entity.SyllabusUnit;
import com.example.manageeducation.enums.SyllabusDayStatus;
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
public class SyllabusDayViewResponse {
    private UUID id;

    private int dayNo;

    @Enumerated(EnumType.STRING)
    private SyllabusDayStatus status;

    private List<SyllabusUnitResponse> syllabusUnits;
}
