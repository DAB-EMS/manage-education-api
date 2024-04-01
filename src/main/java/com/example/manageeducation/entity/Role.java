package com.example.manageeducation.entity;

import com.example.manageeducation.enums.RoleType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@Entity
@Getter
@Setter
public class Role {
    @Id
    private UUID id;

    @Column(length = 60)
    @Enumerated(EnumType.STRING)
    private RoleType name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "role")
    private List<Customer> customers;
}
