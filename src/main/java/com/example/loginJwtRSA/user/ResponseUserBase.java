package com.example.loginJwtRSA.user;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseUserBase {
    private Long id;
    private String username;
    private String password;
    private String email;
    private String phone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
