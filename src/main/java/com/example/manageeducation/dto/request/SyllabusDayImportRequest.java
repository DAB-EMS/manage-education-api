package com.example.manageeducation.dto.request;

import com.example.manageeducation.enums.SyllabusDayStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SyllabusDayImportRequest {
    private int dayNo;

    private SyllabusDayStatus status;

    private List<SyllabusUnitImportRequest> syllabusUnits;
}
