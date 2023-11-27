package com.oing.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/27
 * Time: 2:07 PM
 */
public class SocialLoginProviderTest {

    @DisplayName("SocialLoginProvider enum 테스트")
    @Test
    void testSocialLoginProviderValues() {
        // Given

        // When
        SocialLoginProvider[] providers = SocialLoginProvider.values();

        // Then
        assertNotNull(providers);
        assertEquals(2, providers.length);
        assertEquals(SocialLoginProvider.APPLE, providers[0]);
        assertEquals(SocialLoginProvider.INTERNAL, providers[1]);
    }

    @DisplayName("SocialLoginProvider valueOf 테스트")
    @Test
    void testSocialLoginProviderValueOf() {
        // Given

        // When
        SocialLoginProvider appleProvider = SocialLoginProvider.valueOf("APPLE");
        SocialLoginProvider internalProvider = SocialLoginProvider.valueOf("INTERNAL");

        // Then
        assertEquals(SocialLoginProvider.APPLE, appleProvider);
        assertEquals(SocialLoginProvider.INTERNAL, internalProvider);
    }
}
