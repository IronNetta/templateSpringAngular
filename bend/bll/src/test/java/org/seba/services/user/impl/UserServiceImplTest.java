package org.seba.services.user.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.seba.entities.User;
import org.seba.exceptions.user.UserAlreadyExistExeption;
import org.seba.exceptions.user.UserNotFoundException;
import org.seba.repositories.UserRepository;
import org.seba.requests.SearchParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");
        mockUser.setEmail("test@example.com");
        mockUser.setPassword("password");
        mockUser.setRole(org.seba.enums.UserRole.USER);
    }

    @Test
    void getUsers_ShouldReturnPageOfUsers_WhenSearchParamsEmpty() {
        // Given
        Page<User> mockPage = new PageImpl<>(List.of(mockUser));
        when(userRepository.findAll(any(PageRequest.class))).thenReturn(mockPage);

        // When
        Page<User> result = userService.getUsers(List.of(), PageRequest.of(0, 10));

        // Then
        assertEquals(1, result.getContent().size());
        assertEquals(mockUser, result.getContent().get(0));
        verify(userRepository).findAll(any(PageRequest.class));
    }

    @Test
    void getUserByEmail_ShouldReturnUser_WhenUserExists() {
        // Given
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));

        // When
        User result = userService.getUserByEmail("test@example.com");

        // Then
        assertEquals(mockUser, result);
        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    void getUserByEmail_ShouldThrowException_WhenUserDoesNotExist() {
        // Given
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserByEmail("nonexistent@example.com");
        });
        
        verify(userRepository).findByEmail("nonexistent@example.com");
    }

    @Test
    void saveUser_ShouldReturnSavedUser_WhenEmailUnique() {
        // Given
        User newUser = new User("newuser", "newuser@example.com", "password");
        when(userRepository.existsByEmail("newuser@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // When
        User result = userService.saveUser(newUser);

        // Then
        assertEquals(mockUser, result);
        verify(userRepository).existsByEmail("newuser@example.com");
        verify(passwordEncoder).encode("password");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void saveUser_ShouldThrowException_WhenEmailAlreadyExists() {
        // Given
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        // When & Then
        assertThrows(UserAlreadyExistExeption.class, () -> {
            User duplicateUser = new User("otheruser", "existing@example.com", "password");
            userService.saveUser(duplicateUser);
        });
        
        verify(userRepository).existsByEmail("existing@example.com");
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser_WhenUserExists() {
        // Given
        User updatedUser = new User("updateduser", "updated@example.com", "newpassword");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
        when(userRepository.existsByEmail("updated@example.com")).thenReturn(false);
        when(passwordEncoder.encode("newpassword")).thenReturn("encodedNewPassword");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // When
        User result = userService.updateUser(updatedUser, "test@example.com");

        // Then
        assertEquals(mockUser, result);
        verify(userRepository).findByEmail("test@example.com");
        verify(userRepository).existsByEmail("updated@example.com");
        verify(passwordEncoder).encode("newpassword");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void updateUser_ShouldThrowException_WhenOriginalUserDoesNotExist() {
        // Given
        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> {
            User updatedUser = new User("updateduser", "updated@example.com", "newpassword");
            userService.updateUser(updatedUser, "nonexistent@example.com");
        });
        
        verify(userRepository).findByEmail("nonexistent@example.com");
    }

    @Test
    void updateUser_ShouldThrowException_WhenNewEmailAlreadyExists() {
        // Given
        User updatedUser = new User("updateduser", "existing@example.com", "newpassword");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        // When & Then
        assertThrows(UserAlreadyExistExeption.class, () -> {
            userService.updateUser(updatedUser, "test@example.com");
        });
        
        verify(userRepository).findByEmail("test@example.com");
        verify(userRepository).existsByEmail("existing@example.com");
    }

    @Test
    void deleteUser_ShouldDeleteUser_WhenUserExists() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        // When
        userService.deleteUser(1L);

        // Then
        verify(userRepository).findById(1L);
        verify(userRepository).delete(mockUser);
    }

    @Test
    void deleteUser_ShouldThrowException_WhenUserDoesNotExist() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> {
            userService.deleteUser(999L);
        });
        
        verify(userRepository).findById(999L);
    }

    @Test
    void getUserById_ShouldReturnUser_WhenUserExists() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        // When
        User result = userService.getUserById(1L);

        // Then
        assertEquals(mockUser, result);
        verify(userRepository).findById(1L);
    }

    @Test
    void getUserById_ShouldThrowException_WhenUserDoesNotExist() {
        // Given
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> {
            userService.getUserById(999L);
        });
        
        verify(userRepository).findById(999L);
    }
}