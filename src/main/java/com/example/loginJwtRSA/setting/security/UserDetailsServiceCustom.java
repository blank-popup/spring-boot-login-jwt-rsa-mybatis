package com.example.loginJwtRSA.setting.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceCustom implements UserDetailsService {
    private final MapperAuth mapperAuth;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        return mapperAuth.selectUserDetailsById(Long.parseLong(id))
                .map(user -> {
                    List<Sign.Role> roles = mapperAuth.selectRolesById(Long.parseLong(id));
                    user.setRoles(roles);
                    return user;
                })
                .orElseThrow(() -> new RuntimeException("Cannot find such username[" +  id + "]"));
    }
}
