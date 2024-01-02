package com.oing.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

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
        String familyId = "sampleFamilyId";
        LocalDate dayofBirth = LocalDate.of(2023, 7, 8);
        String name = "sampleName";

        // When
        Member member = new Member(memberId, familyId, dayofBirth, name, null);

        // Then
        assertNotNull(member);
        assertEquals(memberId, member.getId());
    }

    @DisplayName("Member equals, hashCode 테스트")
    @Test
    void testMemberEqualsAndHashCode() {
        // Given
        String memberId = "sampleId";
        String familyId = "sampleFamilyId";
        LocalDate dayofBirth = LocalDate.of(2023, 7, 8);
        String name = "sampleName";

        // When
        Member member1 = new Member(memberId, familyId, dayofBirth, name, null);
        Member member2 = new Member(memberId, familyId, dayofBirth, name, null);

        // Then
        assertEquals(member1, member2);
        assertEquals(member1.hashCode(), member2.hashCode());
    }
}
