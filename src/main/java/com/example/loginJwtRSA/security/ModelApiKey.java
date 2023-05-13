package com.example.loginJwtRSA.security;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ModelApiKey {
    private Long id;
    private Long idUser;
    private String apiKey;
    private LocalDateTime expireAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
