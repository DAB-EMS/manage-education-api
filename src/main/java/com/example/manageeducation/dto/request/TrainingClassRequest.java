package com.example.manageeducation.dto.request;

import com.example.manageeducation.entity.ClassCalendar;
import com.example.manageeducation.entity.Customer;
import com.example.manageeducation.entity.TrainingProgram;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TrainingClassRequest {
    private String name;

    private String courseCode;

    @JsonFormat(pattern="HH:mm:ss")
    private LocalTime startTime;

    @JsonFormat(pattern="HH:mm:ss")
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

    private Set<CustomerRequest> account_trainers;
    private List<CustomerRequest> account_trainee;
    private Set<ClassCalendarRequest> classCalendars;

    private FsuRequest fsu;
}
