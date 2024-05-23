package com.example.manageeducation.trainingclassservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CalendarClassResponse {
    private String name;
    private String code;

    private LocalDateTime dateTime;

    private LocalTime beginTime;

    private LocalTime endTime;

    private String createBy;
}
