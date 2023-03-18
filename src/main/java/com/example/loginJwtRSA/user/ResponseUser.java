package com.example.loginJwtRSA.user;

import lombok.Data;

@Data
public class ResponseUser {
    private Long id;
    private String username;
    private String password;
}
