package org.seba.entities;

import org.junit.jupiter.api.Test;
import org.seba.enums.UserRole;
import org.seba.enums.UserStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void userConstructor_ShouldInitializeFieldsCorrectly() {
        // When
        User user = new User("testuser", "test@example.com", "password");

        // Then - getUsername() returns email for UserDetails implementation
        assertEquals("test@example.com", user.getUsername());  // Returns email as per UserDetails interface
        assertEquals("test@example.com", user.getEmail());     // Direct email field
        assertEquals("password", user.getPassword());          // Direct password field
    }

    @Test
    void getAuthorities_ShouldReturnRoleAsAuthority() {
        // Given
        User user = new User("testuser", "test@example.com", "password");
        user.setRole(UserRole.ADMIN);

        // When
        Collection<? extends org.springframework.security.core.GrantedAuthority> authorities = user.getAuthorities();

        // Then
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority(UserRole.ADMIN.toString())));
    }

    @Test
    void getUsername_ShouldReturnEmail() {
        // Given
        User user = new User("testuser", "test@example.com", "password");

        // When & Then
        assertEquals("test@example.com", user.getUsername());
    }

    @Test
    void isAccountNonExpired_ShouldReturnTrue() {
        // Given
        User user = new User("testuser", "test@example.com", "password");

        // When & Then
        assertTrue(user.isAccountNonExpired());
    }

    @Test
    void isAccountNonLocked_ShouldReturnTrue() {
        // Given
        User user = new User("testuser", "test@example.com", "password");

        // When & Then
        assertTrue(user.isAccountNonLocked());
    }

    @Test
    void isCredentialsNonExpired_ShouldReturnTrue() {
        // Given
        User user = new User("testuser", "test@example.com", "password");

        // When & Then
        assertTrue(user.isCredentialsNonExpired());
    }

    @Test
    void isEnabled_ShouldReturnTrue_WhenStatusIsActive() {
        // Given
        User user = new User("testuser", "test@example.com", "password");
        user.setStatus(UserStatus.ACTIVE);

        // When & Then
        assertTrue(user.isEnabled());
    }

    @Test
    void isEnabled_ShouldReturnFalse_WhenStatusIsNotActive() {
        // Given
        User user = new User("testuser", "test@example.com", "password");
        user.setStatus(UserStatus.DEACTIVATED);

        // When & Then
        assertFalse(user.isEnabled());
    }

    @Test
    void equalsAndHashCode_ShouldWorkCorrectly() {
        // Given
        User user1 = new User("testuser1", "test@example.com", "password");
        User user2 = new User("testuser2", "test@example.com", "password"); // Same email as user1
        User user3 = new User("testuser1", "other@example.com", "password"); // Different email

        user1.setId(1L);
        user2.setId(1L); // Same ID to ensure equality is based on email from @EqualsAndHashCode configuration
        user3.setId(3L);

        // Then - equals based on email (as configured in @EqualsAndHashCode)
        assertEquals(user1, user2); // Same email, so equal
        assertNotEquals(user1, user3); // Different email, so not equal

        // HashCode should be the same for users with same email
        assertEquals(user1.hashCode(), user2.hashCode());
    }
}