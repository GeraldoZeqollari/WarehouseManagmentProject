package com.project.warehousemanagement.persistence.dto;

import com.project.warehousemanagement.constants.UserRole;
import com.project.warehousemanagement.persistence.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class UserDto {

    private UUID id;
    private String username;
    private UserRole role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserDto fromEntity(User user) {
        UserDto userDto = new UserDto();
        userDto.id = user.getId();
        userDto.username = user.getUsername();
        userDto.role = user.getRole();
        userDto.createdAt = user.getCreatedAt();
        userDto.updatedAt = user.getUpdatedAt();
        return userDto;
    }

    public User toEntity() {
        User user = new User();
        user.setUsername(username);
        user.setRole(role);
        user.setCreatedAt(createdAt != null ? createdAt : LocalDateTime.now());
        user.setUpdatedAt(updatedAt);
        return user;
    }
}