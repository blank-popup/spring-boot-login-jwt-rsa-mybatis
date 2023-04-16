package com.example.loginJwtRSA.user;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MapperUser {
    List<ResponseInformation> getUserAll();
    ResponseInformation getUserByUsername(String username);
    int createUser(RequestInformation requestInformation);
    int putUser(RequestInformation requestInformation);
    int patchUser(RequestInformation requestInformation);
    int removeUser(String username);
    int createUserImage(RequestImage requestImage);
    String getFilenameClientByFilenameServer(String filenameServer);
}
