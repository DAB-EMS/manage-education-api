package com.example.manageeducation.syllabusservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ViewSyllabusResponse {
    private UUID id;
    private String name;
    private String code;
    private Date createOn;
    private String createBy;
    private int duration;
    List<OutputStandardResponse> outputStandard;
}
