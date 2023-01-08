package com.example.loginJwtRSA.security;

import lombok.Data;

@Data
public class ModelUser {
    private int id;
    private String username;
    private String password;
}
