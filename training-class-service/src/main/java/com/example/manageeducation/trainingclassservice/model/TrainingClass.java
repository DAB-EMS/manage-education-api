package com.example.manageeducation.trainingclassservice.model;

import com.example.manageeducation.trainingclassservice.enums.TrainingClassStatus;
import com.example.manageeducation.trainingclassservice.utils.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Builder
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

    @JsonIgnore
    @JoinColumn(name = "created_by_id")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID createdBy; // FK

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtils.DATETIME_FORMAT)
    @DateTimeFormat(pattern = DateTimeUtils.DATETIME_FORMAT)
    @CreatedDate
    private LocalDateTime createdDate;

    @JsonIgnore
    @JoinColumn(name = "updated_by_id")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID updatedBy; // FK

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtils.DATETIME_FORMAT)
    @DateTimeFormat(pattern = DateTimeUtils.DATETIME_FORMAT)
    @LastModifiedDate
    private LocalDateTime updatedDate;

    @JsonIgnore
    @JoinColumn(name = "reviewed_by_id")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID reviewedBy;

    private LocalDateTime reviewedDate;

    @JsonIgnore
    @JoinColumn(name = "approved_by_id")
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID approvedBy;

    private LocalDateTime approvedDate;

    private String universityCode;

    private int plannedAttendee;
    private int acceptedAttendee;
    private int actualAttendee;

    @Enumerated(EnumType.ORDINAL)
    private TrainingClassStatus status;

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

    @JsonIgnore
    @JoinColumn(name = "training_program_id", unique = true)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID trainingProgram;

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JsonIgnore
//    @JoinTable(name = "class_trainers", joinColumns = @JoinColumn(name = "class_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "trainer_id", referencedColumnName = "id"))
//    private Set<Customer> account_trainers;
//
//    @ManyToMany(fetch = FetchType.EAGER)
//    @JsonIgnore
//    @JoinTable(name = "class_admins", joinColumns = @JoinColumn(name = "class_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "admin_id", referencedColumnName = "id"))
//    private Set<Customer> account_admins;
//
//    @ManyToMany(fetch = FetchType.EAGER)
//    @JsonIgnore
//    @JoinTable(name = "class_trainee", joinColumns = @JoinColumn(name = "class_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "trainee_id", referencedColumnName = "id"))
//    private List<Customer> account_trainee;

    @ElementCollection
    private Set<UUID> accountTrainers;

    @ElementCollection
    private Set<UUID> accountAdmins;

    @ElementCollection
    private List<UUID> accountTrainee;

    @OneToMany(mappedBy = "trainingClass")
    @JsonIgnore
    private Set<ClassCalendar> classCalendars;
}
