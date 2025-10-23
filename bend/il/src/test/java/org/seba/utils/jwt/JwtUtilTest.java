package org.seba.utils.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.seba.entities.User;
import org.seba.enums.UserRole;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;
    private User testUser;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        testUser = new User("testuser", "test@example.com", "password");  // Use constructor to properly set both username and email
        testUser.setId(1L);
        testUser.setRole(UserRole.USER);
    }

    @Test
    void generateToken_ShouldReturnValidToken() {
        // When
        String token = jwtUtil.generateToken(testUser);

        // Then
        assertNotNull(token);
        assertTrue(token.split("\\.").length == 3); // JWT has 3 parts
    }

    @Test
    void getUsername_ShouldReturnCorrectUsername() {
        // Given
        String token = jwtUtil.generateToken(testUser);

        // When
        String username = jwtUtil.getUsername(token);

        // Then
        assertEquals("test@example.com", username);
    }

    @Test
    void getUserId_ShouldReturnCorrectUserId() {
        // Given
        String token = jwtUtil.generateToken(testUser);

        // When
        Long userId = jwtUtil.getUserId(token);

        // Then
        assertEquals(1L, userId);
    }

    @Test
    void getRole_ShouldReturnCorrectRole() {
        // Given
        String token = jwtUtil.generateToken(testUser);

        // When
        String role = jwtUtil.getRole(token);

        // Then
        assertEquals(UserRole.USER.toString(), role);
    }

    @Test
    void isValid_ShouldReturnTrueForValidToken() {
        // Given
        String token = jwtUtil.generateToken(testUser);

        // When
        boolean isValid = jwtUtil.isValid(token);

        // Then
        assertTrue(isValid);
    }

    @Test
    void getUsername_ShouldThrowExceptionForInvalidToken() {
        // Given
        String invalidToken = "invalid.token.format";

        // When & Then
        assertThrows(Exception.class, () -> {
            jwtUtil.getUsername(invalidToken);
        });
    }
}