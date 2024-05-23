package com.example.manageeducation.trainingclassservice.dto.response;

import com.example.manageeducation.trainingclassservice.dto.response.AttendLevelResponse;
import com.example.manageeducation.trainingclassservice.dto.response.ClassStatusResponse;
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
public class TrainingClassesResponse {
    private UUID id;
    private String name;
    private String code;
    private String createdBy;
    private LocalDateTime createdDate;
    private int duration;
    private String location;
    private String fsu;
    private AttendLevelResponse attend;
    private ClassStatusResponse status;
}
