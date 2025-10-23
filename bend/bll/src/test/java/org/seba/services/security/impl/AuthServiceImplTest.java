package org.seba.services.security.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.seba.entities.User;
import org.seba.enums.UserRole;
import org.seba.exceptions.user.BadCredentialsException;
import org.seba.exceptions.user.UserNotFoundException;
import org.seba.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("password");
        mockUser.setRole(UserRole.USER);
    }

    @Test
    void register_ShouldSaveUser_WhenEmailUnique() {
        // Given
        User newUser = new User("newuser", "newuser@example.com", "password");
        when(userRepository.existsByEmail("newuser@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // When
        authService.register(newUser);

        // Then
        verify(userRepository).existsByEmail("newuser@example.com");
        verify(passwordEncoder).encode("password");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_ShouldThrowException_WhenEmailAlreadyExists() {
        // Given
        User duplicateUser = new User("otheruser", "existing@example.com", "password");
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        // When & Then
        assertThrows(UserNotFoundException.class, () -> {
            authService.register(duplicateUser);
        });
        
        verify(userRepository).existsByEmail("existing@example.com");
    }

    @Test
    void login_ShouldReturnUser_WhenCredentialsValid() {
        // Given
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("password", "password")).thenReturn(true);

        // When
        User result = authService.login("test@example.com", "password");

        // Then
        assertEquals(mockUser, result);
        verify(userRepository).findByEmail("test@example.com");
        verify(passwordEncoder).matches("password", "password");
    }

    @Test
    void login_ShouldThrowException_WhenUserDoesNotExist() {
        // Given
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> {
            authService.login("nonexistent@example.com", "password");
        });
        
        verify(userRepository).findByEmail("nonexistent@example.com");
    }

    @Test
    void login_ShouldThrowException_WhenPasswordInvalid() {
        // Given
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.matches("wrongpassword", "password")).thenReturn(false);

        // When & Then
        assertThrows(BadCredentialsException.class, () -> {
            authService.login("test@example.com", "wrongpassword");
        });
        
        verify(userRepository).findByEmail("test@example.com");
        verify(passwordEncoder).matches("wrongpassword", "password");
    }

    @Test
    void loadUserByUsername_ShouldReturnUserDetails_WhenUserExists() {
        // Given
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));

        // When
        UserDetails result = authService.loadUserByUsername("test@example.com");

        // Then
        assertEquals(mockUser, result);
        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    void loadUserByUsername_ShouldThrowException_WhenUserDoesNotExist() {
        // Given
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> {
            authService.loadUserByUsername("nonexistent@example.com");
        });
        
        verify(userRepository).findByEmail("nonexistent@example.com");
    }
}