package com.example.manageeducation.entity;

import com.example.manageeducation.Utils.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@Entity
@Getter
@Setter
public class TrainingClass implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;

    private String name;

    // courseCode bên DTO
    @Column(unique = true)
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

    private int duration; // month

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "created_by_id")
    private Customer createdBy; // FK

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtils.DATETIME_FORMAT)
    @DateTimeFormat(pattern = DateTimeUtils.DATETIME_FORMAT)
    @CreatedDate
    private LocalDateTime createdDate;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "updated_by_id")
    private Customer updatedBy; // FK

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtils.DATETIME_FORMAT)
    @DateTimeFormat(pattern = DateTimeUtils.DATETIME_FORMAT)
    @LastModifiedDate
    private LocalDateTime updatedDate;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "reviewed_by_id")
    private Customer reviewedBy;

    private LocalDateTime reviewedDate;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "approved_by_id")
    private Customer approvedBy;

    private LocalDateTime approvedDate;

    private String universityCode;

    private int plannedAttendee;
    private int acceptedAttendee;
    private int actualAttendee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "class_location_id", nullable = true)
    private ClassLocation classLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "attendee_level_id", nullable = true)
    private AttendLevel attendeeLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "format_type_id", nullable = true)
    private FormatType formatType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "class_status_id", nullable = true)
    private ClassStatus classStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "technical_group_id", nullable = true)
    private TechnicalGroup technicalGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "program_content_id", nullable = true)
    private ProgramContent programContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "fsu_id", nullable = true)
    private Fsu fsu;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "training_program_id")
    private TrainingProgram trainingProgram;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinTable(name = "class_trainers", joinColumns = @JoinColumn(name = "class_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "trainer_id", referencedColumnName = "id"))
    private Set<Customer> account_trainers;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinTable(name = "class_admins", joinColumns = @JoinColumn(name = "class_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "admin_id", referencedColumnName = "id"))
    private Set<Customer> account_admins;

    @ManyToMany(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinTable(name = "class_trainee", joinColumns = @JoinColumn(name = "class_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "trainee_id", referencedColumnName = "id"))
    private List<Customer> account_trainee; // relationship association

    @OneToMany(mappedBy = "trainingClass")
    @JsonIgnore
    private Set<ClassCalendar> classCalendars;
}
