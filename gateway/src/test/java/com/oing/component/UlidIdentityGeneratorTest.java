package com.oing.component;

import com.oing.util.IdentityGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/27
 * Time: 2:21 PM
 */
@SpringBootTest
@ActiveProfiles("test")
public class UlidIdentityGeneratorTest {

    @Autowired
    private UlidIdentityGenerator ulidIdentityGenerator;

    @Test
    void testUlidIdentityGeneratorImplementsInterface() {
        // Given

        // When
        boolean isIdentityGenerator = ulidIdentityGenerator != null && ulidIdentityGenerator instanceof IdentityGenerator;

        // Then
        assertTrue(isIdentityGenerator, "UlidIdentityGenerator should implement IdentityGenerator interface");
    }

    @Test
    void testGenerateIdentity() {
        // Given

        // When
        String generatedIdentity = ulidIdentityGenerator.generateIdentity();

        // Then
        assertNotNull(generatedIdentity);
        assertTrue(generatedIdentity.matches("[0-9A-Z]{26}"), "Generated identity should be a valid ULID");
    }
}
