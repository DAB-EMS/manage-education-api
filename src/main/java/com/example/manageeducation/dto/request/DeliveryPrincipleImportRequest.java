package com.example.manageeducation.dto.request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class DeliveryPrincipleImportRequest {
    private String trainees;
    private String trainer;
    private String training;
    private String re_test;
    private String marking;
    private String waiverCriteria;
    private String others;
}
