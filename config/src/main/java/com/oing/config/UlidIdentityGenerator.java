package com.oing.config;

import com.github.f4b6a3.ulid.UlidCreator;
import com.oing.util.IdentityGenerator;
import org.springframework.stereotype.Component;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/27
 * Time: 11:48 AM
 */
@Component
public class UlidIdentityGenerator implements IdentityGenerator {
    @Override
    public String generateIdentity() {
        return UlidCreator.getMonotonicUlid().toString();
    }
}
