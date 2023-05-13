package com.example.loginJwtRSA.security;

import lombok.Data;

@Data
public class RequestApiKey {
    private Long idUser;
    private Long terms;
    private String apiKey;
}
