package com.example.manageeducation.trainingclassservice.dto.response;

import com.example.manageeducation.trainingclassservice.enums.TrainingClassStatus;
import com.example.manageeducation.trainingclassservice.model.ClassCalendar;
import com.example.manageeducation.trainingclassservice.utils.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
public class TrainingClassViewResponse {
    private UUID id;

    private String name;

    private String courseCode;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtils.TIME_FORMAT)
    @DateTimeFormat(pattern = DateTimeUtils.TIME_FORMAT)
    private LocalTime startTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtils.TIME_FORMAT)
    @DateTimeFormat(pattern = DateTimeUtils.TIME_FORMAT)
    private LocalTime endTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtils.DATE_FORMAT)
    @DateTimeFormat(pattern = DateTimeUtils.DATE_FORMAT)
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtils.DATE_FORMAT)
    @DateTimeFormat(pattern = DateTimeUtils.DATE_FORMAT)
    private LocalDate endDate;

    private int duration;

    private CustomerCollapseResponse createdBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtils.DATETIME_FORMAT)
    @DateTimeFormat(pattern = DateTimeUtils.DATETIME_FORMAT)
    @CreatedDate
    private LocalDateTime createdDate;

    private CustomerCollapseResponse updatedBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtils.DATETIME_FORMAT)
    @DateTimeFormat(pattern = DateTimeUtils.DATETIME_FORMAT)
    @LastModifiedDate
    private LocalDateTime updatedDate;

    private CustomerCollapseResponse reviewedBy;

    private LocalDateTime reviewedDate;

    private CustomerCollapseResponse approvedBy;

    private LocalDateTime approvedDate;

    private String universityCode;

    private int plannedAttendee;
    private int acceptedAttendee;
    private int actualAttendee;

    @Enumerated(EnumType.STRING)
    private TrainingClassStatus status;

    private ClassLocationResponse classLocation;

    private AttendLevelResponse attendeeLevel;

    private FormatTypeResponse formatType;

    private ClassStatusResponse classStatus;

    private TechnicalGroupResponse technicalGroup;

    private ProgramContentResponse programContent;

    private FsuResponse fsu;

    private TrainingProgramResponse trainingProgram;

    private Set<CustomerCollapseResponse> account_trainers;

    private Set<CustomerCollapseResponse> account_admins;

    private List<CustomerCollapseResponse> account_trainee;

    private Set<ClassCalendar> classCalendars;
}
