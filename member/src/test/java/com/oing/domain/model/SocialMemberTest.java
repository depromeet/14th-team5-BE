package com.oing.domain.model;

import com.oing.domain.SocialLoginProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/27
 * Time: 2:12 PM
 */
public class SocialMemberTest {

    @DisplayName("SocialMember 생성자 테스트")
    @Test
    void testSocialMemberConstructorAndGetters() {
        // Given
        SocialLoginProvider provider = SocialLoginProvider.APPLE;
        String identifier = "user123";
        Member member = new Member("sampleId");

        // When
        SocialMember socialMember = new SocialMember(provider, identifier, member);

        // Then
        assertNotNull(socialMember);
        assertEquals(provider, socialMember.getProvider());
        assertEquals(identifier, socialMember.getIdentifier());
        assertEquals(member, socialMember.getMember());
    }

    @DisplayName("SocialMember setter 테스트")
    @Test
    void testSocialMemberSetter() {
        // Given
        SocialLoginProvider provider = SocialLoginProvider.APPLE;
        String identifier = "user123";
        Member member = new Member("sampleId");
        SocialMember socialMember = new SocialMember();

        // When
        socialMember.setProvider(provider);
        socialMember.setIdentifier(identifier);
        socialMember.setMember(member);

        // Then
        assertEquals(provider, socialMember.getProvider());
        assertEquals(identifier, socialMember.getIdentifier());
        assertEquals(member, socialMember.getMember());
    }

    @DisplayName("SocialMember equals, hashCode 테스트")
    @Test
    void testSocialMemberEqualsAndHashCode() {
        // Given
        SocialLoginProvider provider = SocialLoginProvider.APPLE;
        String identifier = "user123";
        Member member = new Member("sampleId");

        SocialMember socialMember1 = new SocialMember(provider, identifier, member);
        SocialMember socialMember2 = new SocialMember(provider, identifier, member);

        // Then
        assertEquals(socialMember1, socialMember2);
        assertEquals(socialMember1.hashCode(), socialMember2.hashCode());
    }
}
