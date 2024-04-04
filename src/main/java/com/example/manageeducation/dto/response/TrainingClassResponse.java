package com.example.manageeducation.dto.response;

import com.example.manageeducation.entity.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TrainingClassResponse {
    private UUID id;

    private String name;

    private String courseCode;

    private int duration;

    private Customer createdBy;

    private LocalDateTime createdDate;

    private ClassLocationResponse classLocation;

    private AttendLevelResponse attendeeLevel;

    private FsuResponse fsu;
}
