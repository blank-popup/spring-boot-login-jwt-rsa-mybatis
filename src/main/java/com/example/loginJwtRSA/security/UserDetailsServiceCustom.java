package com.example.loginJwtRSA.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserDetailsServiceCustom implements UserDetailsService {
    private final MapperAuth mapperAuth;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        return mapperAuth.selectUserDetailsById(Long.parseLong(id))
                .map(user -> {
                    List<ModelRole> modelRoles = mapperAuth.selectRolesById(Long.parseLong(id));
                    user.setModelRoles(modelRoles);
                    return user;
                })
                .orElseThrow(() -> new RuntimeException("Cannot find such username[" +  id + "]"));
    }
}
