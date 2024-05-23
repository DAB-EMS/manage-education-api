package com.example.manageeducation.trainingclassservice.dto.response;

import com.example.manageeducation.trainingclassservice.enums.Gender;
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