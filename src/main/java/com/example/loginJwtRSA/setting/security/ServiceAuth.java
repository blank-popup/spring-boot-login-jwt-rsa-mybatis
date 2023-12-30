package com.example.loginJwtRSA.setting.security;

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
    public Sign.SignedUp signup(Sign.SigningUp signingUp) {
        log.info("ROLES : {}", signingUp.getRoles());
        if (mapperAuth.selectUserDetailsByUsername(signingUp.getUsername()).isPresent() == true) {
            throw new RuntimeException("Username exist already");
        }

        signingUp.setPassword(passwordEncoder.encode(signingUp.getPassword()));
        mapperAuth.createUser(signingUp);

        UserDetailsCustom newUser = mapperAuth.selectUserDetailsByUsername(signingUp.getUsername()).get();
        List<Sign.Role> rolesSign = mapperAuth.selectRole();
        List<String> rolesUser = new ArrayList<>();
        if (signingUp.getRoles() != null) {
            for (String role : signingUp.getRoles()) {
                Sign.UserRole modelUserRole = new Sign.UserRole();
                modelUserRole.setIdUser(newUser.getId());
                for (Sign.Role roleSign : rolesSign) {
                    if (role.equals(roleSign.getName())) {
                        modelUserRole.setIdRole(roleSign.getId());
                        mapperAuth.insertUserRole(modelUserRole);
                        rolesUser.add(role);
                    }  
                }
            }
        }

        Sign.SignedUp signedUp = new Sign.SignedUp();
        signedUp.setId(newUser.getId());
        signedUp.setUsername(signingUp.getUsername());
        signedUp.setRoles(rolesUser);

        return signedUp;
    }

    public Sign.SignedIn signin(Sign.SigningIn signingIn) {
        UserDetailsCustom userDetailsCustom = mapperAuth.selectUserDetailsByUsername(signingIn.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(signingIn.getPassword(), userDetailsCustom.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        List<Sign.Role> roles = mapperAuth.selectRolesByUsername(signingIn.getUsername());
        userDetailsCustom.setRoles(roles);

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String ip = ClientInfo.getRemoteIP(request);
        String userAgent = request.getHeader("User-Agent");
        log.info("Client Information : IP Address : [{}]", ip);
        log.info("Client Information : User-Agent : [{}]", userAgent);

        String token = providerJwt.createJwt(userDetailsCustom.getId(), ip, userAgent);

        Sign.SignedIn signedIn = new Sign.SignedIn();
        signedIn.setId(userDetailsCustom.getId());
        signedIn.setUsername(userDetailsCustom.getUsername());
        signedIn.setToken(token);
        signedIn.setRoles(userDetailsCustom.getRoles());

        return signedIn;
    }

    public Sign.RegisteredApiKey registerApiKey(Sign.RegisteringApiKey registeringApiKey) {
        String apiKeyValue = providerApiKey.createApiKey(registeringApiKey.getIdUser(), registeringApiKey.getTerms());

        Sign.ApiKey apiKey = new Sign.ApiKey();
        apiKey.setIdUser(registeringApiKey.getIdUser());
        apiKey.setApiKey(apiKeyValue);

        Date now = new Date();
        LocalDateTime expireAt = TimeTable
                .convertMilliSecondsToLocalDateTime(
                        now.getTime() + registeringApiKey.getTerms()
                );
        apiKey.setExpireAt(expireAt);

        int count = mapperAuth.createUserApiKey(apiKey);
        if (count == 1) {
            registeringApiKey.setApiKey(apiKey.getApiKey());
            Sign.RegisteredApiKey registeredApiKey = mapperAuth.getApiKeyByApiKey(registeringApiKey);
            return registeredApiKey;
        }

        return null;
    }
}
