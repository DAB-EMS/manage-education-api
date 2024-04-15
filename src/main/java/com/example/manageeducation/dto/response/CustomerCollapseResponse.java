package com.example.manageeducation.dto.response;

import com.example.manageeducation.enums.Gender;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerCollapseResponse {
    private String id;

    @NonNull
    private String email;
    private String fullName;

    private Date birthday;
    private Gender gender;
}
