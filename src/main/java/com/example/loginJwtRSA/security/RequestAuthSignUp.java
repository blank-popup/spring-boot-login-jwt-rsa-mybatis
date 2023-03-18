package com.example.loginJwtRSA.security;

import lombok.Data;

import java.util.List;

@Data
public class RequestAuthSignUp {
    private String username;
    private String password;
    private List<String> roles;
}
