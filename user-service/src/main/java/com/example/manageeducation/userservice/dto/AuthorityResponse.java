package com.example.manageeducation.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthorityResponse {
    private UUID id;

    @NotBlank
    private String permission;

    @NotBlank
    private String resource;
}
