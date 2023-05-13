package com.example.loginJwtRSA.security;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseApiKey {
    private Long id;
    private Long idUser;
    private String apiKey;
    private LocalDateTime expireAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
