package com.example.manageeducation.entity;

import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Entity
@Getter
@Setter
public class SyllabusUnitChapter {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    private String name;
    private double duration;
    private boolean isOnline;

    @OneToMany(mappedBy = "unitChapter", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Material> materials;
    // unit_id
//	@JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "unit_id")
    private SyllabusUnit syllabusUnit;

    // output_standard_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "output_standard_id")
    private OutputStandard outputStandard;

    // delivery_type_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_type_id")
    private DeliveryType deliveryType;
}
