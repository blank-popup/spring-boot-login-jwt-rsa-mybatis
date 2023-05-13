package com.example.loginJwtRSA.security;

import lombok.Data;

@Data
public class ModelUserRole {
    private Long id;
    private Long idUser;
    private Long idRole;
}
