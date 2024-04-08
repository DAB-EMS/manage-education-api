package com.example.manageeducation.entity;

import com.example.manageeducation.enums.CustomerStatus;
import com.example.manageeducation.enums.Gender;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.Instant;
import java.util.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Getter
@Setter
public class Customer implements UserDetails {
    @Id
    private String id;

    @NonNull
    private String email;
    private String avatar;
    private String password;

    private Instant expiredDate;
    private Instant createdDate;
    private Instant updatedDate;
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

    @OneToOne(mappedBy = "user")
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
