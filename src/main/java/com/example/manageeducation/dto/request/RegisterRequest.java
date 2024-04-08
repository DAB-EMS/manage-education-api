package com.example.manageeducation.dto.request;

import com.example.manageeducation.entity.Role;
import com.example.manageeducation.enums.Gender;
import lombok.*;

import java.util.Date;
import java.util.UUID;

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
