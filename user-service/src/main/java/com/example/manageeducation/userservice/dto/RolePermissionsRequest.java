package com.example.manageeducation.userservice.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RolePermissionsRequest {
    private UUID roleId;
    private List<UUID> authoritiesId;
}
