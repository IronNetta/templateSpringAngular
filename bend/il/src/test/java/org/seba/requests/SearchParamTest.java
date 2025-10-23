package org.seba.requests;

import org.junit.jupiter.api.Test;
import org.seba.entities.User;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SearchParamTest {

    @Test
    void searchParamRecord_ShouldCreateWithCorrectValues() {
        // When
        SearchParam<User> searchParam = new SearchParam<>("username", SearchOperator.EQ, "testuser");

        // Then
        assertEquals("username", searchParam.field());
        assertEquals(SearchOperator.EQ, searchParam.op());
        assertEquals("testuser", searchParam.value());
    }

    @Test
    void createFromEntry_WithSimpleKey_ShouldSetDefaultOperator() {
        // Given
        Map.Entry<String, String> entry = Map.entry("username", "testuser");

        // When
        SearchParam<User> result = SearchParam.create(entry);

        // Then
        assertEquals("username", result.field());
        assertEquals(SearchOperator.EQ, result.op());
        assertEquals("testuser", result.value());
    }

    @Test
    void createFromEntry_WithOperatorInKey_ShouldSetCorrectOperator() {
        // Given
        Map.Entry<String, String> entry = Map.entry("contains_username", "test");

        // When
        SearchParam<User> result = SearchParam.create(entry);

        // Then
        assertEquals("username", result.field());
        assertEquals(SearchOperator.CONTAINS, result.op());
        assertEquals("test", result.value());
    }

    @Test
    void createFromMap_ShouldFilterOutPaginationParams() {
        // Given
        Map<String, String> params = Map.of(
                "username", "testuser",
                "contains_email", "test",
                "page", "1",
                "size", "10",
                "sort", "username"
        );

        // When
        List<SearchParam<User>> result = SearchParam.create(params);

        // Then
        assertEquals(2, result.size());
        
        // Check that both fields are present without relying on order
        List<String> fields = result.stream()
                .map(SearchParam::field)
                .toList();
        assertTrue(fields.contains("username"));
        assertTrue(fields.contains("email"));
    }

    @Test
    void createFromMap_WithEmptyMap_ShouldReturnEmptyList() {
        // Given
        Map<String, String> params = Map.of();

        // When
        List<SearchParam<User>> result = SearchParam.create(params);

        // Then
        assertTrue(result.isEmpty());
    }
}