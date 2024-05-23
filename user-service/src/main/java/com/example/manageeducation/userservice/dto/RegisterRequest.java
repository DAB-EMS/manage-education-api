package com.example.manageeducation.userservice.dto;

import com.example.manageeducation.userservice.enums.Gender;
import com.example.manageeducation.userservice.model.Role;
import lombok.*;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterRequest {
    private String id;
    private String name;
    private String avatar;
    private String email;
    private String password;
    private Date birthday;
    private String level;
    private Role role;
    private Gender gender;
}
