package com.example.manageeducation.trainingclassservice.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DeliveryPrincipleResponse {
    private UUID id;
    private String trainees;
    private String trainer;
    private String training;
    private String re_test;
    private String marking;
    private String waiverCriteria;
    private String others;
}
