package com.example.manageeducation.trainingprogramservice.dto;

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
public class MaterialResponse {
    private UUID id;
    private String name;
    private String url;
    private String createdBy;
    private Date createdDate;
    private String updatedBy;
    private Date updatedDate;
}
