package com.example.manageeducation.securityservice.model;

import com.example.manageeducation.securityservice.enums.TokenType;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
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
    @Column(name = "id", nullable = false)
    private String id;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @NotNull
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID customer;

    @Column(nullable = false, unique = true)
    private String token;

    @Enumerated(EnumType.STRING)
    public TokenType tokenType = TokenType.BEARER;

    public boolean revoked;

    private boolean expiryDate;
}
