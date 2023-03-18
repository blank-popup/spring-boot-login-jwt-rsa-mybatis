package com.example.loginJwtRSA.security;

import lombok.Data;

@Data
public class RequestAuthSignIn {
    private String username;
    private String password;
}
