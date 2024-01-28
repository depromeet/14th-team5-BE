package com.oing.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FamilyMemberProfileResponseTest {
    @DisplayName("FamilyMemberProfileResponse 생성 테스트")
    @Test
    void testFamilyMemberProfileResponse() {
        // given
        String memberId = "1";
        String name = "디프만";
        String imageUrl = "https://asset.no5ing.kr/post/01HGW2N7EHJVJ4CJ999RRS2E97";
        LocalDate dayOfBirth = LocalDate.of(2000, 7, 8);
        LocalDate familyJoinAt = LocalDate.of(2000, 7, 8);
        // when
        FamilyMemberProfileResponse response = new FamilyMemberProfileResponse(memberId, name, imageUrl, dayOfBirth, familyJoinAt);

        // then
        assertEquals(response.memberId(), memberId);
        assertEquals(response.name(), name);
        assertEquals(response.imageUrl(), imageUrl);
        assertEquals(response.dayOfBirth(), dayOfBirth);
        assertEquals(response.familyJoinAt(), familyJoinAt);
    }

}
