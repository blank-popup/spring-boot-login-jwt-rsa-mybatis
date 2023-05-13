package com.example.loginJwtRSA.security;

import com.example.loginJwtRSA.utils.ClientInfo;
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
    private final ProviderJwt providerJwt;
    private final ProviderApiKey providerApiKey;
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
        mapperAuth.createUser(requestSignUp);

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

        String token = providerJwt.createJwt(userDetailsCustom.getId(), ip, userAgent);

        ResponseSignIn responseSignIn = new ResponseSignIn();
        responseSignIn.setId(userDetailsCustom.getId());
        responseSignIn.setUsername(userDetailsCustom.getUsername());
        responseSignIn.setRoles(userDetailsCustom.getRoles());
        responseSignIn.setToken(token);

        return responseSignIn;
    }

    public ResponseApiKey registerApiKey(RequestApiKey requestApiKey) {
        String apiKey = providerApiKey.createApiKey(requestApiKey.getIdUser(), requestApiKey.getTerms());

        ModelApiKey modelApiKey = new ModelApiKey();
        modelApiKey.setIdUser(requestApiKey.getIdUser());
        modelApiKey.setApiKey(apiKey);

        Date now = new Date();
        LocalDateTime expireAt = TimeTable
                .convertMilliSecondsToLocalDateTime(
                        now.getTime() + requestApiKey.getTerms()
                );
        modelApiKey.setExpireAt(expireAt);

        int count = mapperAuth.createUserApiKey(modelApiKey);
        if (count == 1) {
            requestApiKey.setApiKey(modelApiKey.getApiKey());
            ResponseApiKey responseApiKey = mapperAuth.getApiKeyByApiKey(requestApiKey);
            return responseApiKey;
        }

        return null;
    }
}
