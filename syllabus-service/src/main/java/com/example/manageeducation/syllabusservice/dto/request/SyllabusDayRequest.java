package com.example.manageeducation.syllabusservice.dto.request;

import com.example.manageeducation.syllabusservice.enums.SyllabusDayStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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