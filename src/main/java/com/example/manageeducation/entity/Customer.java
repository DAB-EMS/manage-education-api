package com.example.manageeducation.entity;

import com.example.manageeducation.enums.CustomerStatus;
import com.example.manageeducation.enums.Gender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.*;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer"})
public class Customer implements UserDetails {
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
    @Column(columnDefinition = "varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL, FULLTEXT KEY fullname(fullname)")
    private String fullName;

    private Date birthday;
    private Gender gender;
    private String level;

    @OneToOne
    @JoinColumn(name = "fsu_id", nullable = true, unique = true)
    private Fsu fsu;

    @Enumerated(EnumType.ORDINAL)
    private CustomerStatus status;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "createdBy")
    private List<TrainingClass> createdClasses;

    @OneToMany(mappedBy = "updatedBy")
    private List<TrainingClass> updatedClasses;

    @OneToMany(mappedBy = "reviewedBy")
    private List<TrainingClass> reviewedClasses;

    @OneToMany(mappedBy = "approvedBy")
    private List<TrainingClass> approvedClasses;

    @OneToOne(mappedBy = "customer")
    private OTP otp;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
