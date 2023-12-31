package com.example.loginJwtRSA.setting.security;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface MapperAuth {
    Optional<UserDetailsCustom> selectUserDetailsByUsername(String username);
    Optional<UserDetailsCustom> selectUserDetailsById(Long id);
    List<Sign.Role> selectRole();
    int insertUserRole(Sign.UserRole userRole);
    List<Sign.Role> selectRolesByUsername(String username);
    List<Sign.Role> selectRolesById(Long id);
    int createUser(Sign.SigningUp signingUp);
    int createUserApiKey(Sign.ApiKey apiKey);
    Sign.RegisteredApiKey getApiKeyByApiKey(Sign.RegisteringApiKey registeringApiKey);
}
