package com.oing.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/27
 * Time: 2:11 PM
 */
public class MemberTest {

    @DisplayName("Member 생성자 및 getter 테스트")
    @Test
    void testMemberConstructorAndGetters() {
        // Given
        String memberId = "sampleId";

        // When
        Member member = new Member(memberId);

        // Then
        assertNotNull(member);
        assertEquals(memberId, member.getId());
    }

    @DisplayName("Member setter 테스트")
    @Test
    void testMemberSetter() {
        // Given
        String memberId = "sampleId";
        Member member = new Member();

        // When
        member.setId(memberId);

        // Then
        assertEquals(memberId, member.getId());
    }

    @DisplayName("Member equals, hashCode 테스트")
    @Test
    void testMemberEqualsAndHashCode() {
        // Given
        String memberId = "sampleId";
        Member member1 = new Member(memberId);
        Member member2 = new Member(memberId);

        // Then
        assertEquals(member1, member2);
        assertEquals(member1.hashCode(), member2.hashCode());
    }
}
