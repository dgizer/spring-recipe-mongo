package com.springframework.converters;

import com.springframework.commands.RecipeCommand;
import com.springframework.domain.Category;
import com.springframework.domain.Ingredient;
import com.springframework.domain.Notes;
import com.springframework.domain.Recipe;
import com.springframework.enums.Difficulty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.*;

class RecipeToCommandTest {
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

    RecipeToCommand converter;

    @BeforeEach
    void setUp() {
        converter = new RecipeToCommand(new IngredientToCommand(new UnitOfMeasureToCommand()),
                new NotesToCommand(),
                new CategoryToCommand());
    }

    @Test
    void testNullParam() {
        assertNull(converter.convert(null));
    }

    @Test
    void testEmptyParam() {
        assertNotNull(converter.convert(new Recipe()));
        assertThat(converter.convert(new Recipe()), instanceOf(RecipeCommand.class));
    }

    @Test
    void convert() {
        //given
        Recipe recipe = new Recipe();
        recipe.setId(ID);
        recipe.setDescription(DESCRIPTION);
        recipe.setPrepTime(PREP_TIME);
        recipe.setCookTime(COOK_TIME);
        recipe.setServings(SERVINGS);
        recipe.setSource(SOURCE);
        recipe.setUrl(URL);
        recipe.setDirections(DIRECTIONS);

        Ingredient ingr1 = new Ingredient();
        ingr1.setId(ID_ING1);
        Ingredient ingr2 = new Ingredient();
        ingr2.setId(ID_ING2);
        Set<Ingredient> ingredientSet = new HashSet<>();
        ingredientSet.add(ingr1);
        ingredientSet.add(ingr2);

        recipe.setIngredients(ingredientSet);
        recipe.setDifficulty(DIFFICULTY);

        Notes notes = new Notes();
        notes.setId(ID_NOTES);

        recipe.setNotes(notes);

        Category cat1 = new Category();
        cat1.setId(ID_CAT1);
        Category cat2 = new Category();
        cat2.setId(ID_CAT2);
        Set<Category> categorySet = new HashSet<>();
        categorySet.add(cat1);
        categorySet.add(cat2);

        recipe.setCategories(categorySet);

        //when
        RecipeCommand command = converter.convert(recipe);

        //then
        assertNotNull(command);
        assertEquals(ID,command.getId());
        assertEquals(DESCRIPTION,command.getDescription());
        assertEquals(PREP_TIME,command.getPrepTime());
        assertEquals(COOK_TIME,command.getCookTime());
        assertEquals(SERVINGS,command.getServings());
        assertEquals(SOURCE,command.getSource());
        assertEquals(URL,command.getUrl());
        assertEquals(DIRECTIONS,command.getDirections());
        assertEquals(2, command.getIngredients().size());
        assertEquals(DIFFICULTY, command.getDifficulty());
        assertNotNull(command.getNotes());
        assertEquals(ID_NOTES,command.getNotes().getId());
        assertEquals(2, command.getCategories().size());
    }

    @Test
    void convertAddToSetViaGetter() {
        //given
        Recipe recipe = new Recipe();
        recipe.setId(ID);
        recipe.setDescription(DESCRIPTION);
        recipe.setPrepTime(PREP_TIME);
        recipe.setCookTime(COOK_TIME);
        recipe.setServings(SERVINGS);
        recipe.setSource(SOURCE);
        recipe.setUrl(URL);
        recipe.setDirections(DIRECTIONS);

        Ingredient ingr1 = new Ingredient();
        ingr1.setId(ID_ING1);
        Ingredient ingr2 = new Ingredient();
        ingr2.setId(ID_ING2);
        recipe.getIngredients().add(ingr1);
        recipe.getIngredients().add(ingr2);

        recipe.setDifficulty(DIFFICULTY);

        Notes notes = new Notes();
        notes.setId(ID_NOTES);

        recipe.setNotes(notes);

        Category cat1 = new Category();
        cat1.setId(ID_CAT1);
        Category cat2 = new Category();
        cat2.setId(ID_CAT2);

        recipe.getCategories().add(cat1);
        recipe.getCategories().add(cat2);

        //when
        RecipeCommand command = converter.convert(recipe);

        //then
        assertNotNull(command);
        assertEquals(ID,command.getId());
        assertEquals(DESCRIPTION,command.getDescription());
        assertEquals(PREP_TIME,command.getPrepTime());
        assertEquals(COOK_TIME,command.getCookTime());
        assertEquals(SERVINGS,command.getServings());
        assertEquals(SOURCE,command.getSource());
        assertEquals(URL,command.getUrl());
        assertEquals(DIRECTIONS,command.getDirections());
        assertEquals(2, command.getIngredients().size());
        assertEquals(DIFFICULTY, command.getDifficulty());
        assertNotNull(command.getNotes());
        assertEquals(ID_NOTES,command.getNotes().getId());
        assertEquals(2, command.getCategories().size());
    }
}