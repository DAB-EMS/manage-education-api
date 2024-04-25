package com.example.manageeducation.dto.response;

import com.example.manageeducation.entity.ContactPoint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FsuTrainingClassResponse {
    private UUID id;

    private String name;

    private List<ContactPointResponse> contactPoints;
}
