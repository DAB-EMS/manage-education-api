package com.example.manageeducation.dto.request;

import com.example.manageeducation.entity.Syllabus;
import com.example.manageeducation.entity.SyllabusUnit;
import com.example.manageeducation.enums.SyllabusDayStatus;
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
public class SyllabusDayRequest {
    private int dayNo;

    private SyllabusDayStatus status;

    private List<SyllabusUnitRequest> syllabusUnits;
}
