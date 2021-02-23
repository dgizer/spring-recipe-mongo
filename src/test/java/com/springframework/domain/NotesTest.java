package com.springframework.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NotesTest {

    Notes notes;

    @BeforeEach
    void setUp() {
        notes = new Notes();
    }

/*
    @Test
    void getRecipe() {
        Recipe recipe = new Recipe();
        recipe.setId("1L");

        notes.setRecipe(recipe);

        assertNotNull(recipe);
        assertEquals(recipe.getId(), notes.getRecipe().getId());
    }
*/
    @Test
    void getRecipeNotes() {
        String recnotes = "some notes";

        notes.setRecipeNotes(recnotes);

        assertEquals(notes.getRecipeNotes(), recnotes);
    }
}