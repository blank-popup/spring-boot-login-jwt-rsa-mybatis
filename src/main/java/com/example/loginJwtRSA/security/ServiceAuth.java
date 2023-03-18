package com.example.loginJwtRSA.security;

import com.example.loginJwtRSA.utils.ClientInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
//@Transactional(readOnly = true)
public class ServiceAuth {
    private final MapperAuth mapperAuth;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ResponseAuthSignUp signup(RequestAuthSignUp requestAuthSignUp) {
        log.error("ROLES : {}", requestAuthSignUp.getRoles());
        if (mapperAuth.selectUserDetailsByUsername(requestAuthSignUp.getUsername()).isPresent()) {
            throw new RuntimeException("Username exist already");
        }

        requestAuthSignUp.setPassword(passwordEncoder.encode(requestAuthSignUp.getPassword()));
        mapperAuth.insertUser(requestAuthSignUp);

        UserDetailsCustom newUser = mapperAuth.selectUserDetailsByUsername(requestAuthSignUp.getUsername()).get();
        List<ModelRole> modelRoles = mapperAuth.selectRole();
        List<String> roles = new ArrayList<>();
        if (requestAuthSignUp.getRoles() != null) {
            for (String role : requestAuthSignUp.getRoles()) {
                ModelUserRole modelUserRole = new ModelUserRole();
                modelUserRole.setIdUser(newUser.getId());
                for (ModelRole modelRole : modelRoles) {
                    if (role.equals(modelRole.getName())) {
                        modelUserRole.setIdRole(modelRole.getId());
                        mapperAuth.insertUserRole(modelUserRole);
                        roles.add(role);
                    }
                }
            }
        }

        ResponseAuthSignUp responseAuthSignUp = new ResponseAuthSignUp();
        responseAuthSignUp.setId(newUser.getId());
        responseAuthSignUp.setUsername(requestAuthSignUp.getUsername());
        responseAuthSignUp.setRoles(roles);

        return responseAuthSignUp;
    }

    public ResponseAuthSignIn signin(RequestAuthSignIn requestAuthSignIn) {
        UserDetailsCustom userDetailsCustom = mapperAuth.selectUserDetailsByUsername(requestAuthSignIn.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(requestAuthSignIn.getPassword(), userDetailsCustom.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        List<String> roles = mapperAuth.selectRolesByUsername(requestAuthSignIn.getUsername());
        userDetailsCustom.setRoles(roles);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String ip = ClientInfo.getRemoteIP(request);
        String userAgent = request.getHeader("User-Agent");
        log.info("Client Information : IP Address : [{}]", ip);
        log.info("Client Information : User-Agent : [{}]", userAgent);

        String token = jwtTokenProvider.createToken(userDetailsCustom.getId(), ip, userAgent);

        ResponseAuthSignIn responseAuthSignIn = new ResponseAuthSignIn();
        responseAuthSignIn.setId(userDetailsCustom.getId());
        responseAuthSignIn.setUsername(userDetailsCustom.getUsername());
        responseAuthSignIn.setRoles(userDetailsCustom.getRoles());
        responseAuthSignIn.setToken(token);

        return responseAuthSignIn;
    }
}
