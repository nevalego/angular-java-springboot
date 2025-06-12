package com.nveapp.controller;

import com.nveapp.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/secure")
public class SecureController {

    private JwtUtil jwtUtil;

    public SecureController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/test")
    public ResponseEntity<?> testSecureEndpoint(
            @RequestHeader(name = "Authorization", required = false) String authHeader) {

        TokenValidationResult tokenResult = extractToken(authHeader);

        if (!tokenResult.isValid()) {
            return unauthorizedResponse(tokenResult.getError());
        }

        String email = jwtUtil.getEmailFromToken(tokenResult.getToken());
        Map<String, String> response = new HashMap<>();
        response.put("message", "Acceso autorizado");
        response.put("usuario", email);

        return ResponseEntity.ok(response);
    }

    private TokenValidationResult extractToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return TokenValidationResult.invalid("Token no enviado o mal formado");
        }

        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            return TokenValidationResult.invalid("Token inválido o caducado");
        }

        return TokenValidationResult.valid(token);
    }

    private ResponseEntity<Map<String, String>> unauthorizedResponse(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("error", message);
        return ResponseEntity.status(401).body(response);
    }

    private static class TokenValidationResult {
        private final boolean valid;
        private final String token;
        private final String error;

        private TokenValidationResult(boolean valid, String token, String error) {
            this.valid = valid;
            this.token = token;
            this.error = error;
        }

        static TokenValidationResult valid(String token) {
            return new TokenValidationResult(true, token, null);
        }

        static TokenValidationResult invalid(String error) {
            return new TokenValidationResult(false, null, error);
        }

        public boolean isValid() {
            return valid;
        }

        public String getToken() {
            return token;
        }

        public String getError() {
            return error;
        }
    }
}