package org.seba.repositories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.seba.entities.User;
import org.seba.enums.UserRole;
import org.seba.enums.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@EntityScan(basePackages = "org.seba.entities")
@EnableJpaRepositories(basePackages = "org.seba.repositories")
class UserRepositoryTest {
    
    @Configuration
    @ComponentScan(basePackages = {"org.seba.entities", "org.seba.repositories"})
    static class TestConfig {
        // Minimal configuration for test
    }

    @Autowired
    private UserRepository userRepository;

    @Test
    void existsByEmail_ShouldReturnTrue_WhenEmailExists() {
        // Given
        User user = new User("testuser", "test@example.com", "password");
        user.setRole(UserRole.USER);
        userRepository.save(user);

        // When
        boolean exists = userRepository.existsByEmail("test@example.com");

        // Then
        assertTrue(exists);
    }

    @Test
    void existsByEmail_ShouldReturnFalse_WhenEmailDoesNotExist() {
        // Given
        // No user with email "nonexistent@example.com" exists

        // When
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");

        // Then
        assertFalse(exists);
    }

    @Test
    void findByEmail_ShouldReturnUser_WhenEmailExists() {
        // Given
        User user = new User("testuser", "test@example.com", "password");
        user.setRole(UserRole.USER);
        userRepository.save(user);

        // When
        Optional<User> result = userRepository.findByEmail("test@example.com");

        // Then
        assertTrue(result.isPresent());
        assertEquals("test@example.com", result.get().getEmail());
    }

    @Test
    void findByEmail_ShouldReturnEmpty_WhenEmailDoesNotExist() {
        // Given
        // No user with email "nonexistent@example.com" exists

        // When
        Optional<User> result = userRepository.findByEmail("nonexistent@example.com");

        // Then
        assertTrue(result.isEmpty());
    }

    @Test
    void findAll_ShouldReturnAllUsers() {
        // Given
        User user1 = new User("testuser1", "test1@example.com", "password");
        User user2 = new User("testuser2", "test2@example.com", "password");
        user1.setRole(UserRole.USER);
        user2.setRole(UserRole.USER);
        userRepository.save(user1);
        userRepository.save(user2);

        // When
        List<User> users = userRepository.findAll();

        // Then
        assertEquals(2, users.size());
    }
}