package com.springframework.converters;

import com.springframework.commands.IngredientCommand;
import com.springframework.domain.Ingredient;
import com.springframework.domain.Recipe;
import com.springframework.domain.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.*;

class IngredientToCommandTest {
    public static Recipe recipe = new Recipe();
    public static final String RECIPE_ID = "4L";
    public static final String ID = "1L";
    public static final String DESCRIPTION = "Description";
    public static final BigDecimal AMOUNT = BigDecimal.valueOf(10);
    public static final String UOM_ID = "2L";

    UnitOfMeasureToCommand uomConverter;
    IngredientToCommand converter;

    @BeforeEach
    void setUp() {
        converter = new IngredientToCommand(new UnitOfMeasureToCommand());
    }

    @Test
    void testNullParam() {
        assertNull(converter.convert(null));
    }

    @Test
    void testEmptyParam() {
        assertNotNull(converter.convert(new Ingredient()));
        assertThat(converter.convert(new Ingredient()), instanceOf(IngredientCommand.class));
    }

    @Test
    void convert() {
        //given
        recipe.setId(RECIPE_ID);
        Ingredient ingredient = new Ingredient();
        ingredient.setId(ID);
        ingredient.setDescription(DESCRIPTION);
        ingredient.setAmount(AMOUNT);
        UnitOfMeasure uom  = new UnitOfMeasure();
        uom.setId(UOM_ID);
        ingredient.setUom(uom);
        //ingredient.setRecipe(recipe);

        //when
        IngredientCommand command = converter.convert(ingredient);

        //then
        assertNotNull(command);
        assertNotNull(command.getUom());
        //assertEquals(RECIPE_ID,command.getRecipeId());
        assertEquals(ID, command.getId());
        assertEquals(DESCRIPTION, command.getDescription());
        assertEquals(AMOUNT, command.getAmount());
        assertEquals(UOM_ID, command.getUom().getId());
    }

    @Test
    void convertWithNullUom() {
        //given
        Ingredient ingredient = new Ingredient();
        ingredient.setId(ID);
        ingredient.setDescription(DESCRIPTION);
        ingredient.setAmount(AMOUNT);
        ingredient.setUom(null);

        //when
        IngredientCommand command = converter.convert(ingredient);

        //then
        assertNotNull(command);
        assertNull(command.getUom());
        assertEquals(ID,command.getId());
        assertEquals(DESCRIPTION,command.getDescription());
        assertEquals(AMOUNT,command.getAmount());
    }
}