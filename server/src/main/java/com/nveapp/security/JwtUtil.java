package com.nveapp.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;
    private static final long EXPIRATION_TIME = 1000 * 60 * 5; // 5 minutos

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Método para generar un token JWT.
     *
     * @param email Correo electrónico del usuario para el cual se genera el token.
     * @return String representando el token JWT generado.
     */
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Método para validar un token JWT.
     *
     * @param token Token JWT a validar.
     * @return Booleano indicando si el token es válido o no.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Token expirado");
        } catch (Exception e) {
            System.out.println("Token inválido");
        }
        return false;
    }

    /**
     * Método para obtener el correo electrónico del sujeto del token JWT.
     *
     * @param token Token JWT del cual se extrae el correo electrónico.
     * @return String representando el correo electrónico del sujeto del token.
     */
    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Método para refrescar un token JWT.
     * 
     * @param token Token JWT a refrescar.
     * @return String representando el nuevo token JWT generado, o null si el token
     *         original no es válido.
     */
    public String refreshToken(String token) {
        if (!validateToken(token))
            return null;

        String email = getEmailFromToken(token);
        return generateToken(email);
    }

    public Date getExpirationDateFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }
}