package com.example.loginJwtRSA.user;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MapperUser {
    ResponseUserBase getUser(String username);
    int createUser(ModelUserBase modelUserBase);
    int putUser(ModelUserBase modelUserBase);
    int patchUser(ModelUserBase modelUserBase);
    int removeUser(ModelUserBase modelUserBase);
    String getFilenameClientByFilenameServer(String filenameServer);
    int createUserImage(RequestUserImage requestUserImage);
}
