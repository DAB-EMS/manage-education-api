package com.example.manageeducation.trainingclassservice.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class ClassCalendarRequest {
    private int day_no;

    private LocalDateTime dateTime;

    @JsonFormat(pattern="HH:mm:ss")
    private LocalTime beginTime;

    @JsonFormat(pattern="HH:mm:ss")
    private LocalTime endTime;
}
