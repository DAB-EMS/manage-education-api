package com.example.manageeducation.dto.request;

import com.example.manageeducation.entity.Role;
import com.example.manageeducation.enums.CustomerStatus;
import com.example.manageeducation.enums.Gender;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerUpdateRequest {
    @NonNull
    private String email;
    private String fullName;

    private Date birthday;
    private Gender gender;
    private String level;
    private CustomerStatus status;
}
