package com.example.loginJwtRSA.security;

import com.example.loginJwtRSA.user.ServiceUser;
import com.example.loginJwtRSA.utils.Str;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Component
public class AuthorizationDynamic {
    @Autowired
    MapperAuth mapperAuth;
    @Autowired
    ServiceUser serviceUser;

    @Cacheable("check")
    public boolean check(HttpServletRequest request, Authentication authentication) throws Exception {
        String method = request.getMethod();
        String contextPath = request.getContextPath();
        String uri = request.getRequestURI();
        if (method == null) {
            return false;
        }
        log.debug("Authorization : request : method : {}", method);
        log.debug("Authorization : request : contextPath : {}", contextPath);
        log.debug("Authorization : request : uri : {}", uri);

        String uriWithoutContextPath = uri.substring(contextPath.length());
        String uriForAuthorization = uriWithoutContextPath.substring(0, Str.findThIndex(uriWithoutContextPath, "/", 4));
        log.debug("Authorization : request : uriWithoutContextPath : {}", uriWithoutContextPath);
        log.debug("Authorization : request : uriForAuthorization : {}", uriForAuthorization);


        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetailsCustom == false) {
            return false;
        }
        UserDetailsCustom userDetails = (UserDetailsCustom)principal;
        log.debug("userDedatails : {}", userDetails.toString());

        List<ModelRole> modelRoles = userDetails.getModelRoles();

        RequestAuthorization requestAuthorization = new RequestAuthorization();
        requestAuthorization.setMethod(method);
        requestAuthorization.setUrl(uriForAuthorization);
        List<ResponseAuthorization> authorizations = mapperAuth.getAuthorizations(requestAuthorization);
        for (int ii = 0; ii < authorizations.size(); ++ii) {
            log.debug("Response Authorization Dynamic : {}", authorizations.get(ii).toString());
            if (method.equals(authorizations.get(ii).getMethod())) {
                if (modelRoles.indexOf(authorizations.get(ii).getRole()) >= 0) {
                    return true;
                }
            }
            else if (method.equals(authorizations.get(ii).getMethodForSelf())) {
                if (modelRoles.indexOf(authorizations.get(ii).getRole()) >= 0) {
                    if (validateMethodForSelf(uriForAuthorization, method, uriWithoutContextPath, userDetails) == true) {
                        return true;
                    }
                }
            }
        }

        log.warn("Fail to authorize");
        return false;
    }

    public boolean validateMethodForSelf(String uriForAuthorization, String method, String uriWithoutContextPath, UserDetailsCustom userDetails) {
        if ("/api/v1/users".equals(uriForAuthorization) == true) {
            if (serviceUser.validateMethodForSelf(uriForAuthorization, method, uriWithoutContextPath, userDetails) == true) {
                return true;
            }
        }

        return false;
    }
}
