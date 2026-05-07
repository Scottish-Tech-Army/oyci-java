package com.oyci.dto;

import com.oyci.model.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class AuthDtos {

    @Data
    public static class LoginRequest {
        @NotBlank(message = "Username is required")
        private String username;

        @NotBlank(message = "Password is required")
        private String password;
    }

    @Data
    public static class RegisterAdminRequest {
        @NotBlank(message = "Username is required")
        private String username;

        @NotBlank(message = "Password is required")
        private String password;
    }

    @Data
    public static class AuthResponse {
        private String token;
        private String username;
        private Role role;
        private Long userId;
        private String message;

        public AuthResponse(String token, String username, Role role, Long userId) {
            this.token = token;
            this.username = username;
            this.role = role;
            this.userId = userId;
            this.message = "Login successful";
        }
    }
}
