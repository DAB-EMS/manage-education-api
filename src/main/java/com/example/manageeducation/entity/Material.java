package com.example.manageeducation.entity;

import com.example.manageeducation.enums.MaterialStatus;
import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@Entity
@Getter
@Setter
public class Material {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    private String name;
    @Column(length = 65555)
    private String url;
    private UUID createdBy;
    private Date createdDate;
    private UUID updatedBy;
    private Date updatedDate;

    @Lob
    private byte[] data;

    @Enumerated(EnumType.ORDINAL)
    private MaterialStatus materialStatus;

    // unit_chapter_id
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "unit_chapter_id")
    private SyllabusUnitChapter unitChapter;
}
