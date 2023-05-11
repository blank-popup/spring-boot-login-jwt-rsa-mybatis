package com.example.loginJwtRSA.security;

import lombok.Data;

@Data
public class RequestAuthorization {
    private String method;
    private String url;
}
