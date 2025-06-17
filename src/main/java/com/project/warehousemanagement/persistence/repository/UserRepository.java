package com.project.warehousemanagement.persistence.repository;

import com.project.warehousemanagement.constants.UserRole;
import com.project.warehousemanagement.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByUsername(String username);

    List<User> findByRole(UserRole role);

    int countByRole(UserRole role);

    boolean existsByUsername(String username);

    void deleteByUsername(String username);
}
