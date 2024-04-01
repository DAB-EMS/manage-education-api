package com.example.manageeducation.dto.request;

import com.example.manageeducation.entity.Role;
import lombok.*;

import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterRequest {
    private UUID id;
    private String name;
    private String avatar;
    private String email;
    private String password;
    private Role role;
}
