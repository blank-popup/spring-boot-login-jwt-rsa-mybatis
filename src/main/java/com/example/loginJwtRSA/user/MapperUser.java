package com.example.loginJwtRSA.user;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MapperUser {
    ResponseInformation getUserByUsername(String username);
    int createUser(ModelInformation modelInformation);
    int putUser(ModelInformation modelInformation);
    int patchUser(ModelInformation modelInformation);
    int removeUser(ModelInformation modelInformation);
    int createUserImage(RequestImage requestImage);
    String getFilenameClientByFilenameServer(String filenameServer);
}
