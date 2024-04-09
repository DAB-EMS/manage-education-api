package com.example.manageeducation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SyllabusTNImportRequest {
    private String name;
    private String code;
    private String version;
}
