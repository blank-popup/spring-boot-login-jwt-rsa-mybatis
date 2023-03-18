package com.example.loginJwtRSA.security;

import lombok.Data;

import java.util.List;

@Data
public class ResponseAuthSignUp {
    private Long id;
    private String username;
    private List<String> roles;
}
