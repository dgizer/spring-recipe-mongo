package com.springframework.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoryTest {

    Category category;

    @BeforeEach
    public void setUp(){
        category = new Category();
    }


    @Test
    void getId() {
        String idVal = "4L";
        category.setId(idVal);
        assertEquals(idVal, category.getId());
    }

    @Test
    void getDescription() {
        String description = "some description";
        category.setDescription(description);
        assertEquals(description,category.getDescription());
    }

    @Test
    void getRecipes() {
        Recipe recipe1 = new Recipe();
        recipe1.setId("1L");
        Recipe recipe2 = new Recipe();
        recipe2.setId("2L");
        Set<Recipe> recipes = new HashSet<>();
        Collections.addAll(recipes, recipe1,recipe2);
        category.setRecipes(recipes);

        assertEquals(2, category.getRecipes().size());
    }
}