package com.oing.component;

import com.oing.exception.AuthorizationFailedException;
import com.oing.util.AuthenticationHolder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class TokenAuthenticationHolder implements AuthenticationHolder {
    @Override
    public String getUserId() {
        String memberId = SecurityContextHolder.getContext().getAuthentication().getName();
        if (memberId == null) throw new AuthorizationFailedException();
        return memberId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities();
    }
}
