package com.example.manageeducation.entity;

import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.validation.constraints.NotBlank;
import java.util.UUID;
@NoArgsConstructor
@Entity
@Getter
@Setter
public class Authority {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;

    @NotBlank
    private String permission;

    @NotBlank
    private String resource;

    public String appendAuthority(){
        return this.permission.toUpperCase().trim() + "_" +this.resource.toUpperCase().trim();
    }
}
