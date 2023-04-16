package com.example.loginJwtRSA.security;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ResponseApiKey {
    private Long id;
    private String apiKey;
    private LocalDateTime expireAt;
}
