package com.oing.util;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface AuthenticationHolder {
    String getUserId();

    Collection<? extends GrantedAuthority> getAuthorities();
}
