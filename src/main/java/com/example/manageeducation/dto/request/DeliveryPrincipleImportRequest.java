package com.example.manageeducation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DeliveryPrincipleImportRequest {
    private String trainees;
    private String trainer;
    private String training;
    private String re_test;
    private String marking;
    private String waiverCriteria;
    private String others;
}
