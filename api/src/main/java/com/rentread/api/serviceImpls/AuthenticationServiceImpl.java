package com.rentread.api.serviceImpls;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.rentread.api.security.CustomUserDetails;
import com.rentread.api.service.AuthenticationService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService{

	@Override
    public boolean isAuthorized(final String... aInRoles) throws AccessDeniedException {
		CustomUserDetails userDetails = getPrincipal();
        if (userDetails == null) {
            throw new AccessDeniedException("access_denied");
        }

        try {
            for (String role : aInRoles) {
                for (GrantedAuthority authority : userDetails.getAuthorities()) {
                    if (authority.getAuthority().equalsIgnoreCase(role)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            throw new AccessDeniedException("access_denied");
        }

        return false;
    }

    @Override
    public CustomUserDetails getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try {
            return (CustomUserDetails) authentication.getPrincipal();
        } catch (ClassCastException e) {
            log.error("Exception while casting principal to JwtUserDetails, Ex: {}", e.fillInStackTrace());
            return null;
        }
    }
}
