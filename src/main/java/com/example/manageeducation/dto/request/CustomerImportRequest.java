package com.example.manageeducation.dto.request;

import com.example.manageeducation.enums.CustomerStatus;
import com.example.manageeducation.enums.Gender;
import com.example.manageeducation.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerImportRequest {
    private String fullName;
    private Date birthday;
    private Gender gender;
    private String email;
    private String password;
    private RoleType role;
    private String level;
}
