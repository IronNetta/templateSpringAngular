package org.seba.specifications;

import org.junit.jupiter.api.Test;
import org.seba.entities.User;
import org.seba.requests.SearchOperator;
import org.seba.requests.SearchParam;

import static org.junit.jupiter.api.Assertions.*;

class SearchSpecificationTest {

    @Test
    void search_EqualOperator_ShouldCreateEqualSpecification() {
        // Given
        SearchParam<User> searchParam = new SearchParam<>("username", SearchOperator.EQ, "testuser");

        // When
        var specification = SearchSpecification.search(searchParam);

        // Then (just check it's not null)
        assertNotNull(specification);
    }

    @Test
    void search_LikeOperator_ShouldCreateLikeSpecification() {
        // Given
        SearchParam<User> searchParam = new SearchParam<>("email", SearchOperator.CONTAINS, "test");

        // When
        var specification = SearchSpecification.search(searchParam);

        // Then (just check it's not null)
        assertNotNull(specification);
    }

    @Test
    void search_StartOperator_ShouldCreateStartSpecification() {
        // Given
        SearchParam<User> searchParam = new SearchParam<>("username", SearchOperator.START, "test");

        // When
        var specification = SearchSpecification.search(searchParam);

        // Then (just check it's not null)
        assertNotNull(specification);
    }

    @Test
    void search_EndOperator_ShouldCreateEndSpecification() {
        // Given
        SearchParam<User> searchParam = new SearchParam<>("username", SearchOperator.END, "test");

        // When
        var specification = SearchSpecification.search(searchParam);

        // Then (just check it's not null)
        assertNotNull(specification);
    }

    @Test
    void search_NumberOperator_ShouldCreateNumberSpecification() {
        // Given
        SearchParam<User> searchParam = new SearchParam<>("id", SearchOperator.GT, 1L);

        // When
        var specification = SearchSpecification.search(searchParam);

        // Then (just check it's not null)
        assertNotNull(specification);
    }
}