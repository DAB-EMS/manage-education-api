package com.example.manageeducation.syllabusservice.dto.response;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OutputStandardResponse {
    private UUID id;
    private String name;
    private String code;
    private String description;
}
