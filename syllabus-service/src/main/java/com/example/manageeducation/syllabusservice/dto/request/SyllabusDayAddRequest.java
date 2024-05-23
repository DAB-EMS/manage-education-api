package com.example.manageeducation.syllabusservice.dto.request;

import com.example.manageeducation.syllabusservice.enums.SyllabusDayStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SyllabusDayAddRequest {
    private int dayNo;

    private SyllabusDayStatus status;
}
