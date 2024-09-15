package com.example.manageeducation.userservice.dto;

import com.example.manageeducation.userservice.enums.Gender;
import com.example.manageeducation.userservice.enums.RoleType;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CustomerImportRequest {
    private String fullName;
    private Date birthday;
    private Gender gender;
    private String email;
    private String password;
    private RoleType role;
    private String level;
}
