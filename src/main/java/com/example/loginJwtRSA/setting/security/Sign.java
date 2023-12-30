package com.example.loginJwtRSA.setting.security;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

public class Sign {
    @Data
    public static class SigningUp {
        private String username;
        private String password;
        private List<String> roles;
    }

    @Data
    public static class SignedUp {
        private Long id;
        private String username;
        private List<String> roles;
    }

    @Data
    public static class SigningIn {
        private String username;
        private String password;
    }

    @Data
    public static class SignedIn {
        private Long id;
        private String username;
        private String token;
        private List<Sign.Role> roles;
    }

    @Data
    public static class RegisteringApiKey {
        private Long idUser;
        private Long terms;
        private String apiKey;
        }

    @Data
    public static class RegisteredApiKey {
        private Long id;
        private Long idUser;
        private String apiKey;
        private LocalDateTime expireAt;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Data
    public static class Role {
        private Long id;
        private String name;
    }

    @Data
    public static class UserRole {
        private Long id;
        private Long idUser;
        private Long idRole;
    }

    @Data
    public static class ApiKey {
        private Long id;
        private Long idUser;
        private String apiKey;
        private LocalDateTime expireAt;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
