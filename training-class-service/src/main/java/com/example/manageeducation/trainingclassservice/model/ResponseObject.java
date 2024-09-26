package com.example.manageeducation.trainingclassservice.model;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class ResponseObject {
    private String status;
    private String message;
    private Pagination pagination;
    private Object data;
}
