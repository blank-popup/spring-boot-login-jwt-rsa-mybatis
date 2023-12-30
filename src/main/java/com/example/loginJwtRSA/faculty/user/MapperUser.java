package com.example.loginJwtRSA.faculty.user;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MapperUser {
    User.Gotten getUser(User.Getting getting);
    int createUser(User.Creating creating);
    int putUser(User.Putting putting);
    int patchUser(User.Patching patching);
    int removeUser(User.Removing removing);
    UserImage.Downloaded getFileDownload(UserImage.Downloading downloading);
    int createUserImage(UserImage.Uploading uploading);
}
