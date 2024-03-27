package com.example.manageeducation.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@Entity
@Getter
@Setter
public class RefreshToken {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @NotNull
    private Customer customer;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false)
    @NotNull
    private Instant expiryDate;
}
