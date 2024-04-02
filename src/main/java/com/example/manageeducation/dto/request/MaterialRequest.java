package com.example.manageeducation.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Lob;
import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MaterialRequest {
    private String name;
    private String url;
    private UUID createdBy;
    private Date createdDate;
    private UUID updatedBy;
    private Date updatedDate;

    @Lob
    private byte[] data;
}
