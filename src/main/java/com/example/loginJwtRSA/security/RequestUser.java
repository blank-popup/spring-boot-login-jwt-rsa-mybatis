package com.example.loginJwtRSA.security;

import lombok.Data;

@Data
public class RequestUser {
    private String username;
    private String password;
}
