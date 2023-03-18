package com.example.loginJwtRSA.security;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MapperAuth {
    Optional<UserDetailsCustom> selectUserDetailsByUsername(String username);
    Optional<UserDetailsCustom> selectUserDetailsById(Long id);
    List<ModelRole> selectRole();
    int insertUserRole(ModelUserRole modelUserRole);
    List<String> selectRolesByUsername(String username);
    List<String> selectRolesById(Long id);
    int insertUser(RequestAuthSignUp requestAuthSignUp);
}
