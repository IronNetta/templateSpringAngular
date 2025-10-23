package org.seba.entities;

import org.junit.jupiter.api.Test;
import org.seba.entities.base.BaseEntity;

import static org.junit.jupiter.api.Assertions.*;

class BaseEntityTest {

    // Create a concrete implementation for testing purposes
    static class TestEntity extends BaseEntity<Long> {
        private String name;

        public TestEntity() {
            super();
        }

        public TestEntity(String name) {
            super();
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @Test
    void baseEntity_ShouldInitializeIdAndTimestamps() {
        // When
        TestEntity entity = new TestEntity("test");

        // Then - Test id (should be null initially unless set)
        assertNull(entity.getId());

        // Test timestamp methods (should not throw exceptions)
        assertDoesNotThrow(() -> entity.getCreatedAt());
        assertDoesNotThrow(() -> entity.getUpdatedAt());
    }

    @Test
    void equalsAndHashCode_ShouldWorkCorrectly() {
        // Given
        TestEntity entity1 = new TestEntity("test1");
        TestEntity entity2 = new TestEntity("test2");
        TestEntity entity3 = new TestEntity("test3");

        entity1.setId(1L);
        entity2.setId(1L); // Same ID as entity1
        entity3.setId(2L); // Different ID

        // When & Then
        // Entities with same ID should be equal
        assertEquals(entity1, entity2);
        // Entities with different IDs should not be equal
        assertNotEquals(entity1, entity3);

        // HashCode should be the same for entities with same ID
        assertEquals(entity1.hashCode(), entity2.hashCode());
        assertNotEquals(entity1.hashCode(), entity3.hashCode());
    }
}