package com.springframework.converters;

import com.springframework.commands.CategoryCommand;
import com.springframework.commands.IngredientCommand;
import com.springframework.commands.NotesCommand;
import com.springframework.commands.RecipeCommand;
import com.springframework.domain.Recipe;
import com.springframework.enums.Difficulty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.*;

class CommandToRecipeTest {
    public static final String ID = "1L";
    public static final String DESCRIPTION = "Description recipe";
    public static final Integer PREP_TIME = 30;
    public static final Integer COOK_TIME = 15;
    public static final Integer SERVINGS = 10;
    public static final String SOURCE = "Source";
    public static final String URL = "www.example.com";
    public static final String DIRECTIONS = "Directioons";
    public static final String ID_ING1 = "2L";
    public static final String ID_ING2 = "3L";
    public static final Difficulty DIFFICULTY = Difficulty.MODERATE;
    public static final String ID_NOTES = "4L";
    public static final String ID_CAT1 = "5L";
    public static final String ID_CAT2 = "6L";

    CommandToRecipe converter;

    @BeforeEach
    void setUp() {
        converter = new CommandToRecipe(new CommandToIngredient(new CommandToUnitOfMeasure()),
                new CommandToNotes(),
                new CommandToCategory());
    }

    @Test
    void testNullParam() {
        assertNull(converter.convert(null));
    }

    @Test
    void testEmptyParam() {
        assertNotNull(converter.convert(new RecipeCommand()));
        assertThat(converter.convert(new RecipeCommand()), instanceOf(Recipe.class));
    }


    @Test
    void convert() {
        //given
        RecipeCommand command = new RecipeCommand();
        command.setId(ID);
        command.setDescription(DESCRIPTION);
        command.setPrepTime(PREP_TIME);
        command.setCookTime(COOK_TIME);
        command.setServings(SERVINGS);
        command.setSource(SOURCE);
        command.setUrl(URL);
        command.setDirections(DIRECTIONS);

        IngredientCommand ingr1 = new IngredientCommand();
        ingr1.setId(ID_ING1);
        IngredientCommand ingr2 = new IngredientCommand();
        ingr2.setId(ID_ING2);
        List<IngredientCommand> ingredientCommandSet = new ArrayList<>();
        ingredientCommandSet.add(ingr1);
        ingredientCommandSet.add(ingr2);

        command.setIngredients(ingredientCommandSet);
        command.setDifficulty(DIFFICULTY);

        NotesCommand notes = new NotesCommand();
        notes.setId(ID_NOTES);

        command.setNotes(notes);

        CategoryCommand cat1 = new CategoryCommand();
        cat1.setId(ID_CAT1);
        CategoryCommand cat2 = new CategoryCommand();
        cat2.setId(ID_CAT2);
        List<CategoryCommand> categoryCommandSet = new ArrayList<>();
        categoryCommandSet.add(cat1);
        categoryCommandSet.add(cat2);

        command.setCategories(categoryCommandSet);

        //when
        Recipe recipe = converter.convert(command);

        //then
        assertNotNull(recipe);
        assertEquals(ID,recipe.getId());
        assertEquals(DESCRIPTION,recipe.getDescription());
        assertEquals(PREP_TIME,recipe.getPrepTime());
        assertEquals(COOK_TIME,recipe.getCookTime());
        assertEquals(SERVINGS,recipe.getServings());
        assertEquals(SOURCE,recipe.getSource());
        assertEquals(URL,recipe.getUrl());
        assertEquals(DIRECTIONS,recipe.getDirections());
        assertEquals(2, recipe.getIngredients().size());
        assertEquals(DIFFICULTY, recipe.getDifficulty());
        assertNotNull(recipe.getNotes());
        assertEquals(ID_NOTES,recipe.getNotes().getId());
        assertEquals(2, recipe.getCategories().size());        
    }
}