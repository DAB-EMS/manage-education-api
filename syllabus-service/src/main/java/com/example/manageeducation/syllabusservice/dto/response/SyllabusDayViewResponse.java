package com.example.manageeducation.syllabusservice.dto.response;

import com.example.manageeducation.syllabusservice.enums.SyllabusDayStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
