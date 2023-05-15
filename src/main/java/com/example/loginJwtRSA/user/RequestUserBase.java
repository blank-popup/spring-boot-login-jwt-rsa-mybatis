package com.example.loginJwtRSA.user;

import lombok.Data;

@Data
public class RequestUserBase {
    private String username;
    private String password;
    private String passwordOld;
    private String passwordNew;
    private String email;
    private String phone;
}
