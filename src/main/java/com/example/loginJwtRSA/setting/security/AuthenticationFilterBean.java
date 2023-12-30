package com.example.loginJwtRSA.setting.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilterBean extends OncePerRequestFilter {
    private final ProviderJwt providerJwt;
    private final ProviderApiKey providerApiKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.debug("\nExecute Order :\n1. AuthenticationFilterBean.doFilter\n2. UserDetailsServiceCustom.loadUserByUsername\n3. UserDetailsCustom.getAuthorities\n4. AuthorizationDynamic.check");
        while (true) {
            String method = request.getMethod();
            String contextPath = request.getContextPath();
            String requestURI = request.getRequestURI();
            if (method == null || (contextPath != null && requestURI != null && requestURI.equals(contextPath + "/error")) == true) {
                break;
            }
            log.debug("method : contextPath : requestURI : {}, {}, {}", method, contextPath, requestURI);

            String jwt = providerJwt.resolveJwt((HttpServletRequest) request);
            if (StringUtils.hasText(jwt)) {
                Jws<Claims> information = providerJwt.getInformationOfJwt(jwt);
                if (information == null) {
                    break;
                }

                Authentication authentication = providerJwt.getAuthentication(information);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                break;
            }

            String apiKey = providerApiKey.resolveApiKey((HttpServletRequest) request);
            if (StringUtils.hasText(apiKey)) {
                List<String> information = providerApiKey.getInformationOfApiKey(apiKey);
                if (information == null) {
                    break;
                }

                Authentication authentication = providerApiKey.getAuthentication(information);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                break;
            }

            log.warn("There is no valid authentication : {}", requestURI);
            break;
        }

        filterChain.doFilter(request, response);
    }
}
