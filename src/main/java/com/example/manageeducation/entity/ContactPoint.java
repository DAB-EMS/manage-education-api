package com.example.manageeducation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import java.util.UUID;

@NoArgsConstructor
@Entity
@Getter
@Setter
public class ContactPoint {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    private String content;
    private String type;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fsu_id", nullable = false)
    private Fsu fsu;
}
