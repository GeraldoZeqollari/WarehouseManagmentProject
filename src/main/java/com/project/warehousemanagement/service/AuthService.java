package com.project.warehousemanagement.service;


import com.project.warehousemanagement.dto.LoginRequest;
import com.project.warehousemanagement.dto.LoginResponse;
import com.project.warehousemanagement.persistence.entity.User;
import com.project.warehousemanagement.persistence.repository.UserRepository;
import com.project.warehousemanagement.security.JwtUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authManager;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtil jwt;


    public LoginResponse login(LoginRequest req) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.getUsername(), req.getPassword()));

        User user = userRepository.findByUsername(auth.getName()).orElseThrow();

        String token = jwt.generateAccessToken(user.getUsername(), user.getRole());
        String rt = jwt.generateRefreshToken(user.getUsername());
        return new LoginResponse(token, rt);
    }
}