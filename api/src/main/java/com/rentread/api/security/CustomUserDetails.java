package com.rentread.api.security;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.rentread.api.entity.User;

import java.util.Collection;
import java.util.List;

@Data
public final class CustomUserDetails implements UserDetails {
	private static final long serialVersionUID = 1L;
	private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    
    private Collection<? extends GrantedAuthority> authorities;

    /**
     * JwtUserDetails constructor.
     *
     * @param id          String
     * @param email       String
     * @param password    String
     * @param authorities Collection<? extends GrantedAuthority>
     */
    private CustomUserDetails(final Long id, final String email, final String password,
                           final Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    /**
     * Create JwtUserDetails from User.
     *
     * @param user User
     * @return JwtUserDetails
     */
    public static CustomUserDetails create(final User user) {
    	GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().name());
        return new CustomUserDetails(user.getId(), user.getEmail(), user.getPassword(), List.of(authority));
    }
    
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}