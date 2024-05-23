package com.example.manageeducation.trainingclassservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Customer {
    private UUID id;
    private String email;
    private String avatar;
    private String password;
    private Instant expiredDate;
    private Instant createdDate;
    private Instant updatedDate;
    private String fullName;
    private Date birthday;
    private String level;


}
