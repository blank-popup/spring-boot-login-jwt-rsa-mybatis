package com.example.loginJwtRSA.setting.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Component
public class AuthorizationDynamic {
    @Cacheable("check")
    public boolean check(HttpServletRequest request, Authentication authentication) throws Exception {
        String remoteAddr = request.getRemoteAddr();
        String method = request.getMethod();
        String contextPath = request.getContextPath();
        String uri = request.getRequestURI();
        if (method == null) {
            return false;
        }
        log.debug("Authorization : request : remoteAddr, method, contextPath, uri : {}, {}, {}, {}", remoteAddr, method, contextPath, uri);

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetailsCustom == false) {
            return false;
        }
        UserDetailsCustom userDetails = (UserDetailsCustom)principal;
        log.debug("userDedatails : {}", userDetails.toString());

        List<Sign.Role> modelRoles = userDetails.getRoles();
        if (modelRoles.size() > 0) {
            return true;
        }

        log.warn("Fail to authorize");
        return false;
    }
}
