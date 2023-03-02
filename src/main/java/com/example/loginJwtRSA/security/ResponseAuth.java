package com.example.loginJwtRSA.security;

import lombok.Data;

import java.util.List;

@Data
public class ResponseAuth {
    private Long id;
    private String username;
    private String token;
    private List<String> roles;
}
