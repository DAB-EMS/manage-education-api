package com.example.manageeducation.syllabusservice.service.impl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SyllabusUnitAddRequest {
    private String name;
    private int unitNo;
    private int duration;
}
