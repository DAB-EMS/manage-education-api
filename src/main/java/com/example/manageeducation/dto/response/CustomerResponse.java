package com.example.manageeducation.dto.response;

import com.example.manageeducation.entity.Role;
import com.example.manageeducation.enums.CustomerStatus;
import com.example.manageeducation.enums.Gender;
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

    private Date birthday;
    private Gender gender;
    private String level;

    private Role role;
    private CustomerStatus status;
}
