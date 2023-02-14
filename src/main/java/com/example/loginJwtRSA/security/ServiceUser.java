package com.example.loginJwtRSA.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ServiceUser {
    @Autowired
    MapperUser mapperUser;

    public List<ResponseUser> getAllUserInfo() {
        List<ResponseUser> users = mapperUser.getAllUserInfo();
        return users;
    }

    public ResponseUser getUserInfo(RequestUser requestUser) {
        ResponseUser user = mapperUser.getUserInfo(requestUser);
        return user;
    }

    public int createUserInfo(RequestUser requestUser) {
        int result = mapperUser.createUserInfo(requestUser);
        return result;
    }

    public int modifyUserPassword(RequestUser requestUser) {
        int result = mapperUser.modifyUserPassword(requestUser);
        return result;
    }

    public int removeUserInfo(RequestUser requestUser) {
        int reault = mapperUser.removeUserInfo(requestUser);
        return reault;
    }
}
