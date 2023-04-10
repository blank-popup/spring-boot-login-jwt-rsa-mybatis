package com.example.loginJwtRSA.security;

import com.example.loginJwtRSA.utils.ClientInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider implements InitializingBean {
    @Value("${jwt.secret}")
    private String secretKey;
    @Value("${jwt.token-validity-ms}")
    private long tokenValidMillisecond;
    private final UserDetailsService userDetailsService;
    private String key;

    @Override
    public void afterPropertiesSet() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(Long id, String ip, String userAgent) {
        Claims claims = Jwts.claims().setSubject(Long.toString(id));
        claims.put("ip", ip);
        claims.put("userAgent", userAgent);
        for (String key : claims.keySet()) {
            log.info("claims : [{}] : [{}]", key, claims.get(key));
        }
        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidMillisecond))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        String id = getId(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(id);

        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getId(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String resolveToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        }

        return null;
    }

    public boolean validateToken(String token) {
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            String ipRemote = ClientInfo.getRemoteIP(request);
            String userAgentRemote = request.getHeader("User-Agent");

            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            String ipJwt = (String)claims.getBody().get("ip");
            String userAgentJwt = (String)claims.getBody().get("userAgent");
            log.debug("Validate Tocken ipRemote : [{}], ipJwt : [{}]", ipRemote, ipJwt);
            log.debug("Validate Tocken userAgentRemote : [{}], userAgentJwt : [{}]", userAgentRemote, userAgentJwt);

            if (ipRemote != null && ipJwt != null) {
                if (ipRemote.equals(ipJwt) == false) {
                    return false;
                }
            }
            if (userAgentRemote != null && userAgentJwt != null) {
                if (userAgentRemote.equals(userAgentJwt) == false) {
                    return false;
                }
            }
            if (claims.getBody().getExpiration().before(new Date()) == true) {
                return false;
            }

            return true;
        } catch (SecurityException | MalformedJwtException | IllegalArgumentException exception) {
            log.warn("Not available JWT");
        } catch (ExpiredJwtException exception) {
            log.warn("Expired JWT");
        } catch (UnsupportedJwtException exception) {
            log.warn("Not supported JWT");
        }

        return false;
    }
}
