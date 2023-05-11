package com.example.loginJwtRSA.security;

import lombok.Data;

@Data
public class ResponseAuthorization {
    private Long id;
    private String role;
    private String method;
    private String methodForSelf;
    private String url;
}
