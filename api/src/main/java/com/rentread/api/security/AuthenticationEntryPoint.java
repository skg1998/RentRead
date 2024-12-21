package com.rentread.api.security;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rentread.api.dto.AppResponse;
import com.rentread.api.exceptions.AppExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthenticationEntryPoint extends BasicAuthenticationEntryPoint {
	 private final ObjectMapper objectMapper;
	 
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException)
			throws IOException {

        final String unsupported = (String) request.getAttribute("unsupported");
        final String invalid = (String) request.getAttribute("invalid");
        final String illegal = (String) request.getAttribute("illegal");
        final String notfound = (String) request.getAttribute("notfound");
        final String message;

        if (unsupported != null) {
            message = unsupported;
        } else if (invalid != null) {
            message = invalid;
        } else if (illegal != null) {
            message = illegal;
        } else if (notfound != null) {
            message = notfound;
        } else {
            message = authException.getMessage();
        }

        log.error("Could not set user authentication in security context. Error: {}", message);

        ResponseEntity<AppResponse> responseEntity = new AppExceptionHandler()
            .handleBadCredentialsException(new BadCredentialsException(message));
        
        response.addHeader("WWW-Authenticate", "Basic realm= + getRealmName() + ");
        response.getWriter().write(objectMapper.writeValueAsString(responseEntity.getBody()));
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

	}
	
	 @Override
    public void afterPropertiesSet(){
        setRealmName("spring");
        super.afterPropertiesSet();
    }
}