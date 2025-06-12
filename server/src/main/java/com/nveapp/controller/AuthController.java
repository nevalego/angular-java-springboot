package com.nveapp.controller;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nveapp.model.JwtResponse;
import com.nveapp.security.JwtUtil;
import com.nveapp.service.AuthService;

/**
 * Controlador para manejar la autenticación de usuarios.
 * Proporciona endpoints para iniciar sesión, refrescar y validar tokens JWT.
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthService authService;
    private JwtUtil jwtUtil;

    public AuthController(AuthService authService, JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        this.authService = authService;
    }

    /**
     * Método para iniciar sesión de un usuario.
     * 
     * @param request
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String token = authService.login(request.get("email"), request.get("password"));
        return ResponseEntity.ok(new JwtResponse(token));
    }

    /**
     * Método para refrescar un token JWT.
     * 
     * @param token
     * @return
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity
                    .status(401)
                    .body(Collections.singletonMap("error", "Token no enviado o mal formado"));
        }

        String token = authHeader.substring(7);
        String newToken = authService.refresh(token);

        if (newToken == null) {
            return ResponseEntity
                    .status(401)
                    .body(Collections.singletonMap("error", "Token inválido o caducado"));
        }

        Date expiresAt = jwtUtil.getExpirationDateFromToken(newToken);

        Map<String, Object> response = new HashMap<>();
        response.put("token", newToken);
        response.put("expiresAt", expiresAt.toInstant().toString()); // ISO-8601 string

        return ResponseEntity.ok(response);
    }

}
