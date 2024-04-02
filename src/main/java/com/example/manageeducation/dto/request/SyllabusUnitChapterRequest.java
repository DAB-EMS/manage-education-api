package com.example.manageeducation.dto.request;

import com.example.manageeducation.entity.DeliveryType;
import com.example.manageeducation.entity.Material;
import com.example.manageeducation.entity.OutputStandard;
import com.example.manageeducation.entity.SyllabusUnit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
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

    private List<MaterialRequest> materials;

    private DeliveryType deliveryType;
}
