package com.example.loginJwtRSA.security;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MapperUser {
    List<ResponseUser> getAllUserInfo();
    ResponseUser getUserInfo(RequestUser requestUser);
    int createUserInfo(RequestUser requestUser);
    int modifyUserPassword(RequestUser requestUser);
    int removeUserInfo(RequestUser requestUser);
}
