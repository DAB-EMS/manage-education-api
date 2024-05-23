package com.example.manageeducation.userservice.dto;

import com.example.manageeducation.userservice.enums.CustomerStatus;
import com.example.manageeducation.userservice.enums.Gender;
import com.example.manageeducation.userservice.model.Role;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerResponse {
    private UUID id;

    @NonNull
    private String email;
    private String fullName;
    private String avatar;

    private Date birthday;
    private Gender gender;
    private String level;

    private Role role;
    private CustomerStatus status;
}
