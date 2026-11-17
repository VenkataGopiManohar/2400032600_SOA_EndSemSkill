package com.example.endlabexam.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final SecretKey key;
    private final long expirationMs;

    public JwtService(SecretKey jwtSecretKey,
                      @Value("${app.jwt.expiration-ms:3600000}") long expirationMs) {
        this.key = jwtSecretKey;
        this.expirationMs = expirationMs;
    }

    // STEP 1: Parse token, verify signature, validate expiry, and return embedded claims.
    public Claims validateAndExtractClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Demo helper: creates a signed token so the endpoint can be tested in Swagger.
    public String createDemoToken(String username) {
        Date now = new Date();
        return Jwts.builder()
                .subject(username)
                .claims(Map.of("role", "STUDENT", "scope", "read"))
                .issuedAt(now)
                .expiration(new Date(now.getTime() + expirationMs))
                .signWith(key)
                .compact();
    }
}
