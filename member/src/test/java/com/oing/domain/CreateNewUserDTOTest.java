package com.oing.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

        // When
        CreateNewUserDTO createUserDTO = new CreateNewUserDTO(provider, identifier);

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

        // When
        CreateNewUserDTO createUserDTO = new CreateNewUserDTO(provider, identifier);

        // Then
        assertNotNull(createUserDTO);
        assertEquals(provider, createUserDTO.provider());
        assertEquals(identifier, createUserDTO.identifier());
    }
}
