package com.oing.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/27
 * Time: 2:08 PM
 */

public class CreateNewUserDTOTest {

    @DisplayName("CreateNewUserDTO 생성자 테스트 type=APPLE")
    @Test
    void testCreateNewUserDTOConstructor() {
        // Given
        SocialLoginProvider provider = SocialLoginProvider.APPLE;
        String identifier = "user123";
        String memberName = "디프만";
        LocalDate dayOfBirth = LocalDate.of(2000, 7,8);
        String profileImgUrl = "https://picsum.photos/200/300?random=1";

        // When
        CreateNewUserDTO createUserDTO = new CreateNewUserDTO(provider, identifier, memberName, dayOfBirth, profileImgUrl);

        // Then
        assertNotNull(createUserDTO);
        assertEquals(provider, createUserDTO.provider());
        assertEquals(identifier, createUserDTO.identifier());
    }

    @DisplayName("CreateNewUserDTO 생성자 테스트 type=Internal")
    @Test
    void testCreateNewUserDTOValues() {
        // Given
        SocialLoginProvider provider = SocialLoginProvider.INTERNAL;
        String identifier = "admin";
        String memberName = "관리자";
        LocalDate dayOfBirth = LocalDate.of(2000, 7,8);
        String profileImgUrl = "https://picsum.photos/200/300?random=1";

        // When
        CreateNewUserDTO createUserDTO = new CreateNewUserDTO(provider, identifier, memberName, dayOfBirth, profileImgUrl);

        // Then
        assertNotNull(createUserDTO);
        assertEquals(provider, createUserDTO.provider());
        assertEquals(identifier, createUserDTO.identifier());
    }
}
