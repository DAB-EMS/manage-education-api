package com.example.manageeducation.dto.request;

import com.example.manageeducation.enums.TrainingProgramStatus;
import lombok.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TrainingProgramRequest {
    private String name;

    private boolean isTemplate;

    private String version;

    private TrainingProgramStatus status;

    private List<ProgramSyllabusRequest> programSyllabuses;
}
