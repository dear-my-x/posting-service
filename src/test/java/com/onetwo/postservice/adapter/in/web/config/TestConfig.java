package com.onetwo.postservice.adapter.in.web.config;

import com.onetwo.postservice.common.config.filter.AccessKeyCheckFilter;
import com.onetwo.postservice.common.jwt.TokenProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;

import java.io.IOException;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers ->
                        headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorizeHttpRequests ->
                        authorizeHttpRequests.anyRequest().permitAll()
                );

        return httpSecurity.build();
    }

    @Bean
    @Primary
    public TokenProvider tokenProvider() {
        return new TokenProvider() {
            @Override
            public Authentication getAuthentication(String token) {
                return new UsernamePasswordAuthenticationToken(token, token);
            }

            @Override
            public Claims getClaimsByToken(String token) {
                return null;
            }

            @Override
            public boolean validateToken(String token) {
                return false;
            }
        };
    }

    @Bean
    @Primary
    public AccessKeyCheckFilter accessKeyCheckFilter() {
        return new TestAccessKeyCheckFilter();
    }

    class TestAccessKeyCheckFilter extends AccessKeyCheckFilter {
        @Override
        public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            filterChain.doFilter(request, response);
        }
    }
}
