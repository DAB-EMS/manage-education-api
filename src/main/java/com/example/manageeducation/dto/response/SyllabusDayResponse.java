package com.example.manageeducation.dto.response;

import com.example.manageeducation.enums.SyllabusDayStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SyllabusDayResponse {
    private UUID id;

    private int dayNo;

    private SyllabusDayStatus status;
}
