package com.example.manageeducation.trainingprogramservice.dto;

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
public class SyllabusUnitChapterResponse {
    private UUID id;
    private String name;
    private double duration;
    private boolean isOnline;

    private List<MaterialResponse> materials;

    private OutputStandardResponse outputStandard;

    private DeliveryTypeResponse deliveryType;
}
