package com.project.warehousemanagement.seeder;

import com.project.warehousemanagement.constants.UserRole;
import com.project.warehousemanagement.persistence.entity.User;
import com.project.warehousemanagement.persistence.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Configuration
@Slf4j
public class AdminSeeder {

    @Bean
    CommandLineRunner initAdmin(UserRepository users,
                                PasswordEncoder encoder) {
        return args -> {
            if (users.countByRole(UserRole.SYSTEM_ADMIN) == 0) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(encoder.encode("admin123"));
                admin.setRole(UserRole.SYSTEM_ADMIN);
                admin.setCreatedAt(LocalDateTime.now());
                admin.setUpdatedAt(LocalDateTime.now());
                users.save(admin);
                log.info("✔ Created default SYSTEM_ADMIN user → username=admin");
            }
        };
    }
}
