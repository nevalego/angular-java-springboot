# 🚀 Angular & Spring Boot JWT Authentication

This project demonstrates a robust authentication system using JSON Web Tokens (JWT) with an Angular frontend and a Spring Boot backend. It includes features like user login, secure API access, token expiration handling, and token refreshing.

## 📋 Table of Contents

* [Technologies Used](#-technologies-used)
* [Project Structure](#-project-structure)
* [Getting Started](#-getting-started)
    * [Backend Setup](#backend-setup)
    * [Frontend Setup](#frontend-setup)
* [Backend Endpoints](#-backend-endpoints)
    * [1. User Login](#1-user-login)
    * [2. Secure API Test](#2-secure-api-test)
    * [3. Token Expiration Test](#3-token-expiration-test)
    * [4. Refresh Token](#4-refresh-token)
* [SSO (Single Sign-On) Simulation](#-sso-single-sign-on-simulation)
    * [SSO Endpoint Overview](#sso-endpoint-overview)
    * [Testing SSO Flow](#testing-sso-flow)

---

## 💻 Technologies Used

* **Java Version:** 1.8 (compiled with JDK 17)
* **Spring Boot Version:** 2.7.18
* **Spring Boot Dependencies:** Spring Web, Spring Security, JWT
* **Authentication Technology:** JWT (JSON Web Tokens)
* **Frontend:** Angular
* **API Base URL:** `http://localhost:8081`

---

## 📁 Project Structure

The repository is organized into `client` (Angular frontend) and `server` (Spring Boot backend) directories.

.
├── client/
└── server/
└── src/
└── main/
└── java/
└── com/
└── java_app/
├── AppAplication.java
├── controller/        # REST Controllers
├── model/             # Data Models
├── resources/         # Static Resources
├── service/           # Business Logic
└── util/              # Utility Classes (e.g., JWT)



---

## 🚀 Getting Started

Follow these steps to get the project up and running on your local machine.

### Backend Setup

1.  **Navigate to the `server` directory:**
    ```bash
    cd server
    ```
2.  **Clean and install dependencies:**
    ```bash
    mvn clean install
    ```
3.  **Run the Spring Boot application:**
    ```bash
    mvn spring-boot:run
    ```
    The backend API will be available at `http://localhost:8081`.

### Frontend Setup

*(Assuming you have Node.js and Angular CLI installed)*

1.  **Navigate to the `client` directory:**
    ```bash
    cd client
    ```
2.  **Install Node.js dependencies:**
    ```bash
    npm install
    ```
3.  **Start the Angular development server:**
    ```bash
    ng serve --open
    ```
    This will open your application in the browser, usually at `http://localhost:4200`.

---

## 🌐 Backend Endpoints

You can test the API endpoints using tools like [Postman](https://www.postman.com/) or [Insomnia](https://insomnia.rest/).

### 1. User Login

Authenticates a user and issues a JWT.

* **Endpoint:** `POST http://localhost:8081/api/auth/login`
* **Headers:** `Content-Type: application/json`
* **Body (raw, JSON):**
    ```json
    {
      "email": "nerea@correo.com",
      "password": "123456"
    }
    ```
* **Successful Response (200 OK):**
    ```json
    {
      "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuZXJlYUBjb3JyZW8uY29tIiwiaWF0IjoxNzQ5NzI4MTI1LCJleHAiOjE3NDk3Mjg0MjV9.C7YDsupVqVZfZyYYmGShveL9t9tK2V4iQDujJPkRqd8"
    }
    ```
    You can inspect the obtained token at [jwt.io](https://jwt.io/) to verify its payload (e.g., `sub` for user, `iat` for issued at, `exp` for expiration).
    **Example Payload:**
    ```json
    {
      "sub": "nerea@correo.com",
      "iat": 1749728125,
      "exp": 1749728425
    }
    ```

### 2. Secure API Test

Tests access to a protected endpoint using a valid JWT.

* **Endpoint:** `GET http://localhost:8081/api/secure/test`
* **Headers:**
    * `Authorization: Bearer <YOUR_JWT_TOKEN>` (Replace `<YOUR_JWT_TOKEN>` with the token obtained from login).
* **Successful Response (200 OK):**
    ```json
    {
      "usuario": "nerea@correo.com",
      "message": "Acceso autorizado"
    }
    ```
* **Unauthorized Response (401 Unauthorized):**
    If the token is invalid or not provided.
    ```json
    {
      "error": "Token inválido o caducado"
    }
    ```


### 3. Token Expiration Test

Verifies token expiration.

* **Steps:**
    1.  Obtain a fresh token from the `/api/auth/login` endpoint.
    2.  Wait for approximately 5 minutes (or the configured token expiry time).
    3.  Call `GET http://localhost:8081/api/secure/test` again with the expired token.
* **Expected Response (401 Unauthorized):**
    ```json
    {
      "error": "Token inválido o caducado"
    }
    ```

### 4. Refresh Token

Obtains a new JWT using a previously issued (potentially expired) token.

* **Endpoint:** `POST http://localhost:8081/api/auth/refresh`
* **Headers:** `Content-Type: application/json`
* **Body (raw, JSON):**
    ```json
    {
      "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuZXJlYUBjb3JyZW8uY29tIiwiaWF0IjoxNzQ5NzMwODA4LCJleHAiOjE3NDk3MzExMDh9.WTO2XcZaK0yDdbuEWT22pzf-7jxYFo2BjNRy_ieY-is"
    }
    ```
* **Successful Response (200 OK):**
    ```json
    {
      "expiresAt": "2025-06-12T12:25:56Z",
      "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJuZXJlYUBjb3JyZW8uY29tIiwiaWF0IjoxNzQ5NzMwODU2LCJleHAiOjE3NDk3MzExNTZ9.BBYn5f-n_rlKQXh8qXNwOMf3bbDjCDx7eNRQaSm2ftE"
    }
    ```
    The `token` field contains the newly generated JWT, which can be used for subsequent requests.

---

## 🔐 SSO (Single Sign-On) Simulation

This project also includes a simulated Single Sign-On (SSO) flow to demonstrate the general mechanism without requiring a real SSO provider.

### SSO Endpoint Overview

* **`/api/auth/sso` (Redirection Endpoint):**
    * **Method:** `GET`
    * **Purpose:** Simulates the initiation of the SSO authentication flow.
    * **Behavior:** This endpoint generates a simulated redirect URL (containing parameters like a `client_id` and a `redirect_uri` back to the application's callback endpoint). It responds with an HTTP `302 Found` redirection to this simulated SSO provider URL.
    * **Example Flow:** When accessed, it will redirect the browser to `http://localhost:8081/api/auth/sso/callback?code=mock_auth_code_12345&state=random_state_string&redirect_uri=http://localhost:4200/auth/sso-callback`.

* **`/api/auth/sso/callback` (Callback Endpoint):**
    * **Method:** `GET`
    * **Purpose:** Simulates receiving the response from the SSO provider after authentication.
    * **Parameters:** Expects query parameters like `code` (simulated authorization code) and `state`.
    * **Behavior:**
        * Simulates the validation of the authorization code with the "SSO provider".
        * **On Simulated Success:** Returns a JSON response indicating success, similar to the traditional login, including a simulated token or a success message.
            ```json
            {
              "status": "success",
              "message": "SSO authentication successful!",
              "token": "your_simulated_sso_jwt"
            }
            ```
        * **On Simulated Failure:** Returns a JSON response with an error status and a descriptive message (e.g., if the `code` or `state` parameters are incorrect).
            ```json
            {
              "status": "error",
              "message": "SSO authentication failed: code or state incorrect."
            }
            ```
            This might also return an HTTP `400 Bad Request` status code.

### Testing SSO Flow

1.  **Ensure both Backend and Frontend are running.**
2.  **In your Angular application (e.g., `http://localhost:4200/auth/login`), click the "ENTER WITH SSO" button.**
3.  **Observe the Network Tab in your browser's developer tools:**
    * You should see a `GET` request to `http://localhost:8081/api/auth/sso`.
    * This request will respond with a `302 Found` status code, containing a `Location` header pointing to the `/api/auth/sso/callback` endpoint with parameters.
    * The browser will automatically follow this redirection, making a `GET` request to the `/api/auth/sso/callback` endpoint.
    * The `/api/auth/sso/callback` endpoint will respond with a `200 OK` status and a JSON body indicating success or failure, along with a simulated JWT if successful.
    * If successful, your Angular application will then typically navigate to the dashboard (e.g., `/dashboard`).
