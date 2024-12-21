package com.rentread.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

import com.rentread.api.security.AuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
	private final AuthenticationFilter authenticationFilter;
    private final AuthenticationEntryPoint authenticationEntryPoint;
	
	@Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {
        return http
            .csrf(AbstractHttpConfigurer::disable)
            .exceptionHandling(configurer -> configurer.authenticationEntryPoint(authenticationEntryPoint) )
            .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .headers(configurer -> configurer.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
            .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(requests -> requests
                .requestMatchers(
                    "/",
                    "/api/v1/common/**",
                    "/api/v1/user/register",
                    "/api/v1/user/login"
                )
                .permitAll()
                .anyRequest()
                .authenticated()
            )
            .httpBasic(Customizer.withDefaults())
            .build();
    }
}
