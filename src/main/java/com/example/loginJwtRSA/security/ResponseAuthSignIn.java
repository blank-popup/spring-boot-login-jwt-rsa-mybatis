package com.example.loginJwtRSA.security;

import lombok.Data;

import java.util.List;

@Data
public class ResponseAuthSignIn {
    private Long id;
    private String username;
    private String token;
    private List<String> roles;
}
