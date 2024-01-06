package com.oing.domain.key;

import com.oing.domain.SocialLoginProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/27
 * Time: 2:09 PM
 */
public class SocialMemberKeyTest {

    @DisplayName("SocialMemberKey 생성자 테스트")
    @Test
    void testSocialMemberKeyEquality() {
        // Given
        SocialLoginProvider provider = SocialLoginProvider.APPLE;
        String identifier = "user123";

        SocialMemberKey key1 = new SocialMemberKey(provider, identifier);
        SocialMemberKey key2 = new SocialMemberKey(provider, identifier);

        // Then
        assertEquals(key1, key2);
        assertEquals(key1.hashCode(), key2.hashCode());
    }

    @DisplayName("SocialMemberKey 생성자 테스트")
    @Test
    void testSocialMemberKeyInequality() {
        // Given
        SocialMemberKey key1 = new SocialMemberKey(SocialLoginProvider.APPLE, "user123");
        SocialMemberKey key2 = new SocialMemberKey(SocialLoginProvider.INTERNAL, "admin");

        // Then
        assertNotEquals(key1, key2);
        assertNotEquals(key1.hashCode(), key2.hashCode());
    }
}
