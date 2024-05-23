package com.example.manageeducation.syllabusservice.dto.response;

import com.example.manageeducation.syllabusservice.enums.SyllabusDayStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SyllabusDayResponse {
    private UUID id;

    private int dayNo;

    private SyllabusDayStatus status;
}
