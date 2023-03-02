package com.example.loginJwtRSA.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ServiceAuth {
    private final MapperAuth mapperAuth;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserDetailsCustom signup(UserDetailsCustom userDetailsCustom) {
        if (mapperAuth.selectUserDetailsByUsername(userDetailsCustom.getUsername()).isPresent()) {
            throw new RuntimeException("Username exist already");
        }

        userDetailsCustom.setPassword(passwordEncoder.encode(userDetailsCustom.getPassword()));
        mapperAuth.insertUserDetails(userDetailsCustom);

        return mapperAuth.selectUserDetailsByUsername(userDetailsCustom.getUsername()).get();
    }

    public ResponseAuth signin(RequestAuth requestAuth) {
        UserDetailsCustom userDetailsCustom = mapperAuth.selectUserDetailsByUsername(requestAuth.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(requestAuth.getPassword(), userDetailsCustom.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        List<String> roles = mapperAuth.selectRolesByUsername(requestAuth.getUsername());
        userDetailsCustom.setRoles(roles);

        String token = jwtTokenProvider.createToken(userDetailsCustom.getId(), userDetailsCustom.getRoles());

        ResponseAuth responseAuth = new ResponseAuth();
        responseAuth.setId(userDetailsCustom.getId());
        responseAuth.setUsername(userDetailsCustom.getUsername());
        responseAuth.setRoles(userDetailsCustom.getRoles());
        responseAuth.setToken(token);

        return responseAuth;
    }
}
