package com.nveapp.service;

import com.nveapp.exception.InvalidSsoCallbackException;
import com.nveapp.security.JwtUtil;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class SsoService {

    // Simula tu URL base del frontend para el redirect_uri
    @Value("${app.frontend.sso.callback-url:http://localhost:4200/auth/sso-callback}")
    private String frontendSsoCallbackUrl;

    private final JwtUtil jwtUtil;

    public SsoService(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * Simula el inicio del flujo SSO, generando una URL de redirección al
     * "proveedor de SSO".
     * En este caso, redirige directamente a nuestro endpoint de callback simulado.
     *
     * @return La URL completa a la que el cliente debería ser redirigido.
     */
    public String initiateSsoFlow() {
        String simulatedProviderUrl = "http://localhost:8081/api/auth/sso/callback";

        String simulatedAuthCode = "mock_auth_code_12345";
        String simulatedState = "random_state_string";

        return UriComponentsBuilder.fromUriString(simulatedProviderUrl)
                .queryParam("code", simulatedAuthCode)
                .queryParam("state", simulatedState)

                .queryParam("redirect_uri", frontendSsoCallbackUrl) // Frontend callback
                .toUriString();
    }

    /**
     * Simula la validación del código de autorización recibido del "proveedor de
     * SSO".
     *
     * @param authCode El código de autorización simulado.
     * @param state    El estado simulado (para validación CSRF).
     * @return Un token simulado (JWT) si la validación es exitosa.
     * @throws InvalidSsoCallbackException Si el código o estado son inválidos.
     */
    public String validateSsoCallback(String authCode, String state) {
        if ("mock_auth_code_12345".equals(authCode) && "random_state_string".equals(state)) {
            return jwtUtil.generateToken("sso_user@example.com");
        } else {
            throw new InvalidSsoCallbackException("SSO Callback inválido: código de autorización o estado incorrecto.");
        }
    }
}