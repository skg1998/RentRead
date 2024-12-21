package com.rentread.api.service;

import org.springframework.security.access.AccessDeniedException;

import com.rentread.api.security.CustomUserDetails;

public interface AuthenticationService {
	public boolean isAuthorized(final String... aInRoles) throws AccessDeniedException;
	public CustomUserDetails getPrincipal();
}
