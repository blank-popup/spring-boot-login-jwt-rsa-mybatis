package com.example.loginJwtRSA.user;

import lombok.Data;

@Data
public class RequestImage {
    private int id;
    private String description;
    private String filenameServer;
    private String filenameClient;
}
