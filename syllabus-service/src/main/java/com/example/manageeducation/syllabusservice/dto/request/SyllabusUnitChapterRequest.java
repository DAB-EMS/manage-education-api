package com.example.manageeducation.syllabusservice.dto.request;

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
public class SyllabusUnitChapterRequest {
    private String name;
    private double duration;
    private boolean isOnline;
    private UUID outputStandardId;
    private UUID deliveryTypeId;

    private List<MaterialRequest> materials;
}
