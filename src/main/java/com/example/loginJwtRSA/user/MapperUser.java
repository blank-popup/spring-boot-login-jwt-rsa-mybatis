package com.example.loginJwtRSA.user;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MapperUser {
    List<ResponseUser> getUserAll();
    ResponseUser getUserByUsername(RequestUser requestUser);
    int createUser(RequestUser requestUser);
    int modifyUser(RequestUser requestUser);
    int removeUser(RequestUser requestUser);
}
