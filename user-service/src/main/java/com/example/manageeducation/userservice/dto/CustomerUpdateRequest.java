package com.example.manageeducation.userservice.dto;

import com.example.manageeducation.userservice.enums.CustomerStatus;
import com.example.manageeducation.userservice.enums.Gender;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CustomerUpdateRequest {
    @NonNull
    private String email;
    private String fullName;

    private Date birthday;
    private Gender gender;
    private String level;
    private CustomerStatus status;
}
