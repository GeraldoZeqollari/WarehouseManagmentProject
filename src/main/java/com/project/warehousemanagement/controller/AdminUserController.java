package com.project.warehousemanagement.controller;

import com.project.warehousemanagement.constants.UserRole;
import com.project.warehousemanagement.dto.UserRequest;
import com.project.warehousemanagement.persistence.dto.UserDto;
import com.project.warehousemanagement.persistence.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('SYSTEM_ADMIN')")
public class AdminUserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> create(@Valid @RequestBody UserRequest userRequest) {
        log.info("Create user: {}", userRequest.getUsername());
        UserDto body = userService.createUser(userRequest);
        URI location = URI.create("/api/admin/users/" + body.getId());
        return ResponseEntity.created(location).body(body);
    }

    @PutMapping("/update/{username}")
    public ResponseEntity<UserDto> update(@PathVariable String username,
                                          @Valid @RequestBody UserRequest userRequest) {
        return ResponseEntity.ok(userService.updateUser(username,userRequest));
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<Void> delete(@PathVariable String username) {
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> list(@RequestParam(required = false) UserRole userRole) {
        return ResponseEntity.ok(userService.getUsers(userRole));
    }

    @GetMapping("/detail/{username}")
    public ResponseEntity<UserDto> detail(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }
}
