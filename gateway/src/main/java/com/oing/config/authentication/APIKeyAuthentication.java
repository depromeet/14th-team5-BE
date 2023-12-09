package com.oing.config.authentication;

import com.oing.domain.Token;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/25
 * Time: 8:04 PM
 */
@RequiredArgsConstructor
public class APIKeyAuthentication implements Authentication {
    private final Token token;
    private final String userId;
    private final boolean temporary;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (temporary)
            return List.of(new SimpleGrantedAuthority("TEMPORARY_MEMBER"));
        return List.of(new SimpleGrantedAuthority("MEMBER"));
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return "";
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return userId;
    }
}
