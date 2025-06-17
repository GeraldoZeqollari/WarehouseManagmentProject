package com.project.warehousemanagement.persistence.service.impl;

import com.project.warehousemanagement.constants.OrderStatus;
import com.project.warehousemanagement.constants.UserRole;
import com.project.warehousemanagement.dto.UserRequest;
import com.project.warehousemanagement.persistence.dto.UserDto;
import com.project.warehousemanagement.persistence.entity.User;
import com.project.warehousemanagement.persistence.repository.OrderRepository;
import com.project.warehousemanagement.persistence.repository.UserRepository;
import com.project.warehousemanagement.persistence.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final OrderRepository orderRepository;

    @Override
    public UserDto createUser(UserRequest userRequest) {
        log.info("Create user method");
        if (userRepository.findByUsername(userRequest.getUsername()).isPresent())
            throw new IllegalArgumentException("Username already exists");

        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword(encoder.encode(userRequest.getPassword()));
        user.setRole(UserRole.valueOf(userRequest.getRole().name()));
        log.info("Created user {} ", user.getUsername());
        return UserDto.fromEntity(userRepository.save(user));
    }

    @Override
    public UserDto updateUser(String username, UserRequest request) {
        log.info("Updating user {} ", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (request.getUsername() != null &&
                !request.getUsername().equalsIgnoreCase(user.getUsername())) {

            if (userRepository.findByUsername(request.getUsername()).isPresent())
                throw new IllegalArgumentException("Username already exists");

            user.setUsername(request.getUsername());
        }


        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(encoder.encode(request.getPassword()));
        }


        if (request.getRole() != null) {
            user.setRole(UserRole.valueOf(request.getRole().name()));
        }
        log.info("Update user completed");
        return UserDto.fromEntity(userRepository.save(user));
    }

    @Override
    public void deleteUser(String username) {
        log.info("Deleting user {} ", username);
        if (!userRepository.existsByUsername(username))
            throw new EntityNotFoundException("User not found");

        if (orderRepository.findByClientUsername(username).stream().anyMatch(order -> order.getStatus() == OrderStatus.AWAITING_APPROVAL)) {
            throw new IllegalStateException("Order in awaiting state cannot delete user");
        }
        userRepository.deleteByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsers(UserRole roleFilter) {
        log.info("Retrieving all users by role {}", roleFilter);
        if (roleFilter == null) {
            return userRepository.findAll().stream()
                    .map(UserDto::fromEntity)
                    .toList();
        }
        return userRepository.findByRole(roleFilter).stream()
                .map(UserDto::fromEntity)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserDto getUserByUsername(String username) {
        log.info("Retrieving user by username {}", username);
        return userRepository.findByUsername(username)
                .map(UserDto::fromEntity)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

}
