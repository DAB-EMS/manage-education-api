package com.example.manageeducation.dto.request;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TrainingClassRequest {
    private String name;

    private String courseCode;

    private LocalTime startTime;

    private LocalTime endTime;

    private LocalDate startDate;

    private LocalDate endDate;

    private int duration;

    private UUID reviewedBy;

    private LocalDateTime reviewedDate;

    private UUID approvedBy;

    private LocalDateTime approvedDate;

    private String universityCode;

    private int plannedAttendee;
    private int acceptedAttendee;
    private int actualAttendee;

    private ClassLocationRequest classLocation;

    private AttendLevelRequest attendeeLevel;

    private FormatTypeRequest formatType;

    private ClassStatusRequest classStatus;

    private TechnicalGroupRequest technicalGroup;

    private ProgramContentRequest programContent;

    private Set<CustomerRequest> account_admins;

    private FsuRequest fsu;
}
