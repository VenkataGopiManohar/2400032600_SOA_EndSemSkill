package com.example.endlabexam.controller;

import com.example.endlabexam.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jwt")
public class JwtController {
    private final JwtService jwtService;

    public JwtController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    // STEP 2A: Demo token endpoint. Use this only for local lab testing.
    @PostMapping("/demo-token")
    public Map<String, String> createDemoToken(@RequestParam(defaultValue = "student") String username) {
        return Map.of("token", jwtService.createDemoToken(username));
    }

    // STEP 2B: Validation endpoint. Supply the raw JWT in the Authorization header.
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("valid", false, "message", "Missing Bearer token"));
        }

        String token = authorization.substring(7).trim();
        try {
            Claims claims = jwtService.validateAndExtractClaims(token);
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("valid", true);
            result.put("subject", claims.getSubject());
            result.put("issuedAt", claims.getIssuedAt());
            result.put("expiration", claims.getExpiration());
            result.put("claims", claims);
            return ResponseEntity.ok(result);
        } catch (JwtException | IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("valid", false, "message", "Invalid or expired JWT"));
        }
    }
}
