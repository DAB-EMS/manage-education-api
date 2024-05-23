package com.example.manageeducation.syllabusservice.dto.response;

import com.example.manageeducation.syllabusservice.enums.SyllabusStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SyllabusViewProgramResponse {
    private UUID id;
    private String name;
    private String code;
    private String version;
    private int days;
    private int hours;
    private String createdByName;
    private Date createdDate;
    private SyllabusStatus status;
}
