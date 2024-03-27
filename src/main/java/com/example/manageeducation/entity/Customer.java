package com.example.manageeducation.entity;

import com.example.manageeducation.enums.Gender;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@Entity
@Getter
@Setter
public class Customer {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    private String email;
    private String avatar;
    private String password;

    private Instant expiredDate;
    private Instant createdDate;
    private Instant updatedDate;
    private String fullname;

    private Date birthday;
    private Gender gender;
    private String level;

}
