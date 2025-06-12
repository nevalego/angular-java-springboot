# angular-java-springboot


Java Versión 1.8 compilado desde JDK 17
SpringBoot version 2.7.18
Dependencias: Spring Web, Spring Security, JWT
Tecnología de autenticación: JWT

API Rest en http://localhost:8081 con 3 endpoints:

* /api/auth/login
* /api/auth/refresh
* /api/secure/test

## Estructura del proyecto

client
server
    src
        main
            java
                com
                    java_app
                        AppAplication.java
                        controller
                        model
                        resources
                        service
                        util

## Back-end Java SpringBoot 

> mvn clean 
> mvn install
> mvn spring-boot:run

### Pruebas con Postman

1. Probar Login

Se puede probar la API con autenticación JWT usando Postman. Para ello se va a hacer una petición de login para autenticar el usuario:
POST  http://localhost:8081/api/auth/login
Añadiendo en el cuerpo de la petición la opción "raw" con "JSON":

{
  "email": "nerea@correo.com",
  "password": "123456"
}

Al enviar esta petición se obtiene el token de esta forma: 

{
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuZXJlYUBjb3JyZW8uY29tIiwiaWF0IjoxNzQ5NzI4MTI1LCJleHAiOjE3NDk3Mjg0MjV9.C7YDsupVqVZfZyYYmGShveL9t9tK2V4iQDujJPkRqd8"
}

A continuación, se puede probar en https://jwt.io para ver si el token obtenido tiene 
el usuario, la expiración y los datos necesarios. 

Header:
{
  "alg": "HS256"
}
Payload:
{
  "sub": "nerea@correo.com",
  "iat": 1749728125,
  "exp": 1749728425
}

2. Probar token 

GET http://localhost:8081/api/secure/test
con Headers Key: Authorization y Value: Bearer TOKEN

Si el token es válido se recibe:

{
    "usuario": "nerea@correo.com",
    "message": "Acceso autorizado"
}

Si el token es inválido o no lo envías, deberías recibir un código 401 Unauthorized con el mensaje:
{
    "error": "Token inválido o caducado"
}

3. Probar caducidad token

Esperar 5 minutos y volver a llamar a la petición /api/secure/test con el token,
para que devuelva un mensaje:

{
  "error": "Token inválido o caducado"
}

4. Refrescar token

POST http://localhost:8081/api/auth/refresh
mandando en el body, raw, el token :
{
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuZXJlYUBjb3JyZW8uY29tIiwiaWF0IjoxNzQ5NzMwODA4LCJleHAiOjE3NDk3MzExMDh9.WTO2XcZaK0yDdbuEWT22pzf-7jxYFo2BjNRy_ieY-is"
}

obtenemos la fecha de caducidad:

{
    "expiresAt": "2025-06-12T12:25:56Z",
    "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuZXJlYUBjb3JyZW8uY29tIiwiaWF0IjoxNzQ5NzMwODU2LCJleHAiOjE3NDk3MzExNTZ9.BBYn5f-n_rlKQXh8qXNwOMf3bbDjCDx7eNRQaSm2ftE"
}

que puede ser usado en futuras peticiones 