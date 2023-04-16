package com.example.loginJwtRSA.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationFilterBean extends GenericFilterBean {
    private final JwtProvider jwtProvider;
    private final ApiKeyProvider apiKeyProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException, NullPointerException {
        while (true) {
            String requestURI = ((HttpServletRequest) request).getRequestURI();

            String bearer = jwtProvider.resolveToken((HttpServletRequest) request);
            if (StringUtils.hasText(bearer) && jwtProvider.validateToken(bearer)) {
                Authentication authentication = jwtProvider.getAuthentication(bearer);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                break;
            }

            String apiKey = apiKeyProvider.resolveApiKey((HttpServletRequest) request);
            if (StringUtils.hasText(apiKey)) {
                Authentication authentication = apiKeyProvider.getAuthentication(apiKey);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                break;
            }

            log.warn("There is invalid authentication : {}", requestURI);
            break;
        }

        chain.doFilter(request, response);
    }
}
