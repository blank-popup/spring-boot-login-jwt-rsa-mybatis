package com.example.loginJwtRSA.security;

import com.example.loginJwtRSA.utils.Crypto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApiKeyProvider {
    private final MapperAuth mapperAuth;
    private final UserDetailsService userDetailsService;
    @Value("${apikey.secret}")
    private String secretKey;
    @Value("${apikey.iv}")
    private String secretIv;
    @Value("${apikey.delimiter}")
    private String idHashDelimiter;

    public Authentication getAuthentication(String apiKey) throws NullPointerException {
        try {
            String decryptedText = Crypto.decryptAES(apiKey, secretKey, secretIv);
            String id = decryptedText.substring(0, decryptedText.lastIndexOf(idHashDelimiter));
            String hash = decryptedText.substring(decryptedText.lastIndexOf(idHashDelimiter) + idHashDelimiter.length());
            String hashId = Crypto.hashSHA256(id);
            if (hashId == null || hashId.equals(hash) == false) {
                return null;
            }

            ModelApiKey modelApiKey = mapperAuth.getApiKeyByApiKey(apiKey);
            log.info("Api Key authentication : ID : {}", modelApiKey.getId());

            Date now = new Date();
            if (now.getTime() > Timestamp.valueOf(modelApiKey.getExpireAt()).getTime()) {
                log.warn("Expired Api Key : Current time : {}, Expired time : {}", now.getTime(), Timestamp.valueOf(modelApiKey.getExpireAt()).getTime());
                return null;
            }

            UserDetails userDetails = userDetailsService.loadUserByUsername(Long.toString(modelApiKey.getId()));

            return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        }
        catch (NullPointerException e) {
            return null;
        }
    }

    public String resolveApiKey(HttpServletRequest request) {
        String apiKey = request.getHeader("X-API-KEY");

        if (StringUtils.hasText(apiKey)) {
            return apiKey;
        }

        return null;
    }
}
