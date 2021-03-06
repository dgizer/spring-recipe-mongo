package com.springframework.services;

import com.springframework.commands.IngredientCommand;
import com.springframework.commands.UnitOfMeasureCommand;
import com.springframework.converters.CommandToIngredient;
import com.springframework.converters.CommandToUnitOfMeasure;
import com.springframework.converters.IngredientToCommand;
import com.springframework.converters.UnitOfMeasureToCommand;
import com.springframework.domain.Ingredient;
import com.springframework.domain.Recipe;
import com.springframework.domain.UnitOfMeasure;
import com.springframework.repositories.IngredientRepository;
import com.springframework.repositories.RecipeRepository;
import com.springframework.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IngredientServiceImplTest {
    private static final String RECIPE_ID = "1L";
    private static final String ING1_ID = "1L";
    private static final String ING2_ID = "2L";
    private static final String ING3_ID = "3L";

    @Mock
    private RecipeRepository recipeRepository;
    @Mock
    IngredientRepository ingredientRepository;
    @Mock
    private UnitOfMeasureRepository uomRepo;

    private IngredientToCommand ingredientToCommand;
    private CommandToIngredient commandToIngredient;

    private IngredientService ingredientService;

    IngredientServiceImplTest() {
        this.commandToIngredient = new CommandToIngredient(new CommandToUnitOfMeasure());
        this.ingredientToCommand = new IngredientToCommand(new UnitOfMeasureToCommand());
    }

    @BeforeEach
    void setUp() {
        ingredientService = new IngredientServiceImpl(recipeRepository, ingredientRepository, ingredientToCommand, uomRepo ,commandToIngredient);
    }

    @Test
    void findByRecipeIdAndIngredientId() {
        //given
        Recipe recipe = new Recipe();
        recipe.setId(RECIPE_ID);
        Ingredient ingr1 = new Ingredient();
        ingr1.setId(ING1_ID);

        Ingredient ingr2 = new Ingredient();
        ingr2.setId(ING2_ID);
        Ingredient ingr3 = new Ingredient();
        ingr3.setId(ING3_ID);
        recipe.addIngredient(ingr1);
        recipe.addIngredient(ingr2);
        recipe.addIngredient(ingr3);
        Optional<Recipe> recipeOptional = Optional.of(recipe);

        //when
        when(recipeRepository.findById(anyString())).thenReturn(recipeOptional);

        IngredientCommand foundIngredient = ingredientService.findByRecipeIdAndIngredientId(RECIPE_ID,ING2_ID);

        //then
        assertNotNull(foundIngredient);
        assertEquals(ING2_ID,foundIngredient.getId());
        assertEquals(RECIPE_ID,foundIngredient.getRecipeId());
        verify(recipeRepository).findById(anyString());

    }

    @Test
    void saveNewIngredient() {
        //given
        String  id1 = "1L";
        String recId = "2L";
        IngredientCommand command =  new IngredientCommand();
        command.setId(id1);
        command.setRecipeId(recId);
        command.setDescription("descr");
        command.setAmount(BigDecimal.valueOf(10));
        UnitOfMeasureCommand uomcom = new UnitOfMeasureCommand();
        uomcom.setId("10L");
        command.setUom(uomcom);

        Optional<Recipe> recipeOpt = Optional.of(new Recipe());
        Recipe recipeSaved = new Recipe();

        Ingredient ingredient = new Ingredient();
        ingredient.setId(id1);
        ingredient.setDescription("descr");
        ingredient.setAmount(BigDecimal.valueOf(10));
        UnitOfMeasure uom = new UnitOfMeasure();
        uom.setId("10L");
        ingredient.setUom(uom);
        recipeSaved.setId(recId);
        recipeSaved.addIngredient(ingredient);

        when(recipeRepository.findById(anyString())).thenReturn(recipeOpt);
        when(recipeRepository.save(any())).thenReturn(recipeSaved);

        //when
        IngredientCommand savedComamnd = ingredientService.saveIngredient(command);

        //then
        assertNotNull(savedComamnd);
        assertEquals(id1,savedComamnd.getId());
        verify(recipeRepository).findById(anyString());
        verify(recipeRepository).save(any(Recipe.class));
    }

    @Test
    void deleteIngredientById() {
        //given
        String id1 = "1L";
        String id2 = "2L";
        String recId = "1L";

        Ingredient ingr1 = new Ingredient();
        ingr1.setId(id1);
        Ingredient ingr2 = new Ingredient();
        ingr2.setId(id2);

        Recipe recipe = new Recipe();
        recipe.addIngredient(ingr1);
        recipe.addIngredient(ingr2);
        Recipe savedRecipe = new Recipe();
        savedRecipe.setId(recId);
        savedRecipe.addIngredient(ingr2);

        when(recipeRepository.findById(anyString())).thenReturn(Optional.of(recipe));

        //when
        ingredientService.deleteIngredientById(recId,id1);


        //then
        verify(recipeRepository).findById(anyString());
        verify(recipeRepository).save(any());
        verify(ingredientRepository).deleteById(anyString());
        assertFalse(recipe.getIngredients().contains(ingr1));
    }
}