package com.example.loginJwtRSA.user;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MapperUser {
    List<ResponseUser> getUserAll();
    ResponseUser getUserByUsername(String username);
    int createUser(RequestUser requestUser);
    int putUser(RequestUser requestUser);
    int patchUser(RequestUser requestUser);
    int removeUser(String username);
    int createUserImage(RequestUserImage requestUserImage);
    String getFilenameClientByFilenameServer(String filenameServer);
}
