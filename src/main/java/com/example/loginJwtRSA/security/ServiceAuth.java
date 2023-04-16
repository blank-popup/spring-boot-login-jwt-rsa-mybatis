package com.example.loginJwtRSA.security;

import com.example.loginJwtRSA.utils.ClientInfo;
import com.example.loginJwtRSA.utils.Crypto;
import com.example.loginJwtRSA.utils.TimeTable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
//@Transactional(readOnly = true)
public class ServiceAuth {
    private final MapperAuth mapperAuth;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    @Value("${apikey.secret}")
    private String secretKey;
    @Value("${apikey.iv}")
    private String secretIv;
    @Value("${apikey.delimiter}")
    private String idHashDelimiter;

    @Transactional
    public ResponseSignUp signup(RequestSignUp requestSignUp) {
        log.info("ROLES : {}", requestSignUp.getRoles());
        if (mapperAuth.selectUserDetailsByUsername(requestSignUp.getUsername()).isPresent()) {
            throw new RuntimeException("Username exist already");
        }

        requestSignUp.setPassword(passwordEncoder.encode(requestSignUp.getPassword()));
        mapperAuth.insertUser(requestSignUp);

        UserDetailsCustom newUser = mapperAuth.selectUserDetailsByUsername(requestSignUp.getUsername()).get();
        List<ModelRole> modelRoles = mapperAuth.selectRole();
        List<String> roles = new ArrayList<>();
        if (requestSignUp.getRoles() != null) {
            for (String role : requestSignUp.getRoles()) {
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

        ResponseSignUp responseSignUp = new ResponseSignUp();
        responseSignUp.setId(newUser.getId());
        responseSignUp.setUsername(requestSignUp.getUsername());
        responseSignUp.setRoles(roles);

        return responseSignUp;
    }

    public ResponseSignIn signin(RequestSignIn requestSignIn) {
        UserDetailsCustom userDetailsCustom = mapperAuth.selectUserDetailsByUsername(requestSignIn.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(requestSignIn.getPassword(), userDetailsCustom.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        List<String> roles = mapperAuth.selectRolesByUsername(requestSignIn.getUsername());
        userDetailsCustom.setRoles(roles);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String ip = ClientInfo.getRemoteIP(request);
        String userAgent = request.getHeader("User-Agent");
        log.info("Client Information : IP Address : [{}]", ip);
        log.info("Client Information : User-Agent : [{}]", userAgent);

        String token = jwtProvider.createToken(userDetailsCustom.getId(), ip, userAgent);

        ResponseSignIn responseSignIn = new ResponseSignIn();
        responseSignIn.setId(userDetailsCustom.getId());
        responseSignIn.setUsername(userDetailsCustom.getUsername());
        responseSignIn.setRoles(userDetailsCustom.getRoles());
        responseSignIn.setToken(token);

        return responseSignIn;
    }

    public ResponseApiKey createUserApiKey(RequestApiKey requestApiKey) {
        ModelApiKey modelApiKey = new ModelApiKey();
        modelApiKey.setId(requestApiKey.getId());

        Date now = new Date();
        long nowMilliSeconds = now.getTime();
        String id = Long.toString(requestApiKey.getId());
        String hashId = Crypto.hashSHA256(id);
        // Hash of Id : 6b86b273ff34fce19d6b804eff5a3f5747ada4eaa22f1d49c01e52ddb7875b4b
        log.debug("Hash of Id : {}", hashId);
        String plainText = id + idHashDelimiter + hashId;
        String encryptText = Crypto.encryptAES(plainText, secretKey, secretIv);
        // API Key : mqn6nH0/6hJkOOVrJlzuVL9p8bxGPNySp4gKdTwT5hjD+pIWmwGBS5FJJG0o1Vg02tXq1T2ak+mWu7B5M5t13tUxBWQNtTuHoD2/pIpLc2Q=
        log.debug("API Key : {}", encryptText);
        modelApiKey.setApiKey(encryptText);

        LocalDateTime expireAt = TimeTable
                .convertMilliSecondsToLocalDateTime(
                        nowMilliSeconds + requestApiKey.getTerms()
                );
        modelApiKey.setExpireAt(expireAt);

        int count = mapperAuth.createUserApiKey(modelApiKey);
        if (count == 1) {
            ResponseApiKey responseApiKey = new ResponseApiKey();
            responseApiKey.setId(requestApiKey.getId());
            responseApiKey.setApiKey(encryptText);
            responseApiKey.setExpireAt(expireAt);
            return responseApiKey;
        }

        return null;
    }
}
