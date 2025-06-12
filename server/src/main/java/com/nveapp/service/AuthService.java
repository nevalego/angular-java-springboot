package com.nveapp.service;

import com.nveapp.repository.UserRepository;
import com.nveapp.security.JwtUtil;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private JwtUtil jwtUtil;
    private UserRepository userRepository;

    public AuthService(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    /**
     * Método para simular el inicio de sesión de un usuario.
     * 
     * @param email
     * @param password
     * @return
     */
    public String login(String email, String password) {
        if (userRepository.validateUser(email, password)) {
            return jwtUtil.generateToken(email);
        }
        throw new RuntimeException("Credenciales inválidas");
    }

    /**
     * Método para refrescar un token JWT.
     * 
     * @param token
     * @return
     */
    public String refresh(String token) {
        return jwtUtil.refreshToken(token);
    }
}
