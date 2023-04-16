package com.example.loginJwtRSA.security;

import lombok.Data;

@Data
public class RequestSignIn {
    private String username;
    private String password;
}
