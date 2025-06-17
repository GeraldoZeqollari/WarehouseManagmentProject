package com.project.warehousemanagement.dto;

import com.project.warehousemanagement.constants.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequest {

    @NotBlank
    private String username;

    @NotBlank
    @ToString.Exclude
    private String password;

    @NotNull
    private UserRole role;
}