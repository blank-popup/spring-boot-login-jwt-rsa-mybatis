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
    List<ModelRole> selectRolesByUsername(String username);
    List<ModelRole> selectRolesById(Long id);
    int createUser(RequestSignUp requestSignUp);
    int createUserApiKey(ModelApiKey modelApiKey);
    ResponseApiKey getApiKeyByApiKey(RequestApiKey requestApiKey);
    List<ResponseAuthorization> getAuthorizations(RequestAuthorization requestAuthorization);
}
