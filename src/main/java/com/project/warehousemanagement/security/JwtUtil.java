package com.project.warehousemanagement.security;

import com.project.warehousemanagement.constants.UserRole;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtil {

    private final Key key;
    private final long accessTtl;
    private final long refreshTtl;

    public JwtUtil(@Value("${jwt.secret}") String secret,
                   @Value("${jwt.validity-sec}") long accessMin,
                   @Value("${jwt.refresh-ttl}") long refreshDays) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessTtl = accessMin * 60L;
        this.refreshTtl = refreshDays * 86_400L;
    }


    public String generateAccessToken(String username, UserRole role) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(username)
                .claim("roles", role.name())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(accessTtl)))
                .signWith(key)
                .compact();
    }

    public String generateRefreshToken(String username) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(username)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(refreshTtl)))
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {
        return parser().parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isValid(String token) {
        try {
            parser().parseSignedClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private JwtParser parser() {
        return Jwts.parser().verifyWith((SecretKey) key).build();
    }
}