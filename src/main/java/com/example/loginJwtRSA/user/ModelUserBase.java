package com.example.loginJwtRSA.user;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ModelUserBase {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
