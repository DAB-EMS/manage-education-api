package com.example.manageeducation.userservice.model;

import com.example.manageeducation.userservice.enums.CustomerStatus;
import com.example.manageeducation.userservice.enums.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Customer {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;

    @Column(length = 50)
    @NonNull
    @Email
    private String email;
    @Lob
    private String avatar;
    @JsonIgnore
    @Size(min = 6, max = 100)
    private String password;

    private Instant expiredDate;
    private Instant createdDate;
    private Instant updatedDate;
    private String fullName;

    private Date birthday;
    private Gender gender;
    private String level;

    @JoinColumn(name = "fsu_id", nullable = true, unique = true)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID fsu;

    @Enumerated(EnumType.ORDINAL)
    private CustomerStatus status;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToOne(mappedBy = "customer")
    private OTP otp;
}
