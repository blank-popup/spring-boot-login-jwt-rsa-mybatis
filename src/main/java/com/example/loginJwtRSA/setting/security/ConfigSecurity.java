package com.example.loginJwtRSA.setting.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@RequiredArgsConstructor
public class ConfigSecurity {
    private final ProviderJwt providerJwt;
    private final ProviderApiKey providerApiKey;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", configuration);
        return urlBasedCorsConfigurationSource;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(basic -> basic.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf.disable())
                .sessionManagement(management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeRequests(requests -> requests
                        // http://localhost:18010/local/docs/index.html
                        .antMatchers(HttpMethod.GET, "/docs/**").permitAll()
                        .antMatchers(HttpMethod.POST, "/api/v1/auth/**").permitAll()
                        .antMatchers("/error/**").permitAll()
                        // .antMatchers("/api/vi/**").permitAll()
                        // .antMatchers("/api/v1/user/**").hasAnyRole("USER")
                        // .antMatchers("/api/v1/user/**").hasAnyRole("ADMIN", "MANAGER")
                        // .anyRequest().authenticated()
                        .anyRequest().access("@authorizationDynamic.check(request, authentication)"))
                .exceptionHandling(handling -> handling.accessDeniedHandler(
                        new AccessDeniedHandlerCustom()
                ))
                .exceptionHandling(handling -> handling.authenticationEntryPoint(
                        new AuthenticationEntryPointCustom()
                ))
                .addFilterBefore(
                        new AuthenticationFilterBean(providerJwt, providerApiKey),
                        UsernamePasswordAuthenticationFilter.class
                );
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web
                .ignoring()
                .antMatchers(
//                        "/swagger-resources/**",=
//                        "/swagger-ui.html",
//                        "/swagger/**"
                );
    }
}
