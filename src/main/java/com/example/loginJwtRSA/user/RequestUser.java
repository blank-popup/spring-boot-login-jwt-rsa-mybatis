package com.example.loginJwtRSA.user;

import lombok.Data;

@Data
public class RequestUser {
    private String username;
    private String password;
}
