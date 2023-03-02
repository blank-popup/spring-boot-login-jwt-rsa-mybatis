package com.example.loginJwtRSA.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ServiceUser {
    @Autowired
    MapperUser mapperUser;

    public List<ResponseUser> getAll() {
        List<ResponseUser> users = mapperUser.getAll();
        return users;
    }

    public ResponseUser get(RequestUser requestUser) {
        ResponseUser user = mapperUser.get(requestUser);
        return user;
    }

    public int create(RequestUser requestUser) {
        int result = mapperUser.create(requestUser);
        return result;
    }

    public int modify(RequestUser requestUser) {
        int result = mapperUser.modify(requestUser);
        return result;
    }

    public int remove(RequestUser requestUser) {
        int reault = mapperUser.remove(requestUser);
        return reault;
    }
}
