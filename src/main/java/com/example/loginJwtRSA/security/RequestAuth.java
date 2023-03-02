package com.example.loginJwtRSA.security;

import lombok.Data;

@Data
public class RequestAuth {
    private String username;
    private String password;
}
