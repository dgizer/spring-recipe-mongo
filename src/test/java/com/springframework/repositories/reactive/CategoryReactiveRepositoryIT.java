package com.springframework.repositories.reactive;

import com.springframework.domain.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataMongoTest
class CategoryReactiveRepositoryIT {

    @Autowired
    CategoryReactiveRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll().block();
    }

    @Test
    void testSave() {
        Category category = new Category();
        category.setDescription("cat1");

        repository.save(category).block();

        Long count = repository.count().block();
        assertEquals(1L, count);
    }

    @Test
    void findByDescription() {
        String query = "cat1";
        Category category = new Category();
        category.setDescription(query);

        repository.save(category).block();

        Category found = repository.findByDescription(query).block();

        assertEquals(query, found.getDescription());
    }
}