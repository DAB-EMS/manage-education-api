package com.example.manageeducation.syllabusservice.dto;

import lombok.*;
import java.time.Instant;
import java.util.Date;
import java.util.List;
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
