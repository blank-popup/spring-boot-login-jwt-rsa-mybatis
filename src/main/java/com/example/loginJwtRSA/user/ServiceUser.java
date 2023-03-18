package com.example.loginJwtRSA.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ServiceUser {
    private final MapperUser mapperUser;
    private final PasswordEncoder passwordEncoder;

    public List<ResponseUser> getUserAll() {
        List<ResponseUser> users = mapperUser.getUserAll();
        return users;
    }

    public ResponseUser getUserByUsername(RequestUser requestUser) {
        ResponseUser user = mapperUser.getUserByUsername(requestUser);
        return user;
    }

    public int createUser(RequestUser requestUser) {
        requestUser.setPassword(passwordEncoder.encode(requestUser.getPassword()));
        int result = mapperUser.createUser(requestUser);
        return result;
    }

    public int modifyUser(RequestUser requestUser) {
        requestUser.setPassword(passwordEncoder.encode(requestUser.getPassword()));
        int result = mapperUser.modifyUser(requestUser);
        return result;
    }

    public int removeUser(RequestUser requestUser) {
        int reault = mapperUser.removeUser(requestUser);
        return reault;
    }
}
