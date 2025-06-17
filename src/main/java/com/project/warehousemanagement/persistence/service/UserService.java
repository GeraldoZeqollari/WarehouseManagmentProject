package com.project.warehousemanagement.persistence.service;

import com.project.warehousemanagement.constants.UserRole;
import com.project.warehousemanagement.dto.UserRequest;
import com.project.warehousemanagement.persistence.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(UserRequest dto);

    UserDto updateUser(String username,UserRequest userRequest);

    void deleteUser(String username);

    List<UserDto> getUsers(UserRole roleFilter);

    UserDto getUserByUsername(String username);

}
