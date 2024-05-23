package com.example.manageeducation.syllabusservice.dto;

import com.example.manageeducation.syllabusservice.enums.MaterialStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MaterialDTO {
    private UUID id;
    private String name;
    private String url;
    private String createdBy;
    private Date createdDate;
    private MaterialStatus materialStatus;
}
