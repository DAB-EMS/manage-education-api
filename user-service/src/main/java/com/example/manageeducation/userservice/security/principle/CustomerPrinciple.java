package com.example.manageeducation.userservice.security.principle;

import com.example.manageeducation.userservice.enums.CustomerStatus;
import com.example.manageeducation.userservice.enums.Gender;
import com.example.manageeducation.userservice.model.Authority;
import com.example.manageeducation.userservice.model.Customer;
import com.example.manageeducation.userservice.model.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CustomerPrinciple implements UserDetails {
    private UUID id;
    private String fullName;
    private String email;
    private Instant expiredDate;
    private Date birthday;
    private Instant createdDate;
    @JsonIgnore
    private String password;
    private String avatar;
    private Gender gender;
    private String level;
    private CustomerStatus status;
    private Role role;
    private Set<Authority> authorities;

    @JsonIgnore
    private Collection<? extends GrantedAuthority> grantedAuthorities;

    public static CustomerPrinciple build(Customer user) {
        List<GrantedAuthority> grantedAuthorities = user.getRole().getAuthorities().stream().map(authority -> new SimpleGrantedAuthority(authority.appendAuthority())).collect(Collectors.toList());
        return CustomerPrinciple.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .avatar(user.getAvatar())
                .gender(user.getGender())
                .level(user.getLevel())
                .expiredDate(user.getExpiredDate())
                .status(user.getStatus())
                .authorities(user.getRole().getAuthorities())
                .grantedAuthorities(grantedAuthorities)
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
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

        if (expiredDate != null) {
            return expiredDate.compareTo(new Date().toInstant()) > 0;
        }
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
        return !Objects.equals(status, CustomerStatus.DEACTIVE) && !Objects.equals(status, CustomerStatus.DELETE);
    }

}
