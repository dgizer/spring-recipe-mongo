package com.springframework.repositories.reactive;

import com.springframework.domain.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataMongoTest
class RecipeReactiveRepositoryIT {

    @Autowired
    RecipeReactiveRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll().block();
    }

    @Test
    void testSave() {
        Recipe recipe = new Recipe();
        recipe.setDescription("some");
        repository.save(recipe).block();

        Long count = repository.count().block();

        assertEquals(1L,count);

    }
}