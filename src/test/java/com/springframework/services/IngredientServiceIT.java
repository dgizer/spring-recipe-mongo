package com.springframework.services;

import com.springframework.commands.IngredientCommand;
import com.springframework.converters.CommandToIngredient;
import com.springframework.converters.CommandToUnitOfMeasure;
import com.springframework.converters.IngredientToCommand;
import com.springframework.converters.UnitOfMeasureToCommand;
import com.springframework.domain.Ingredient;
import com.springframework.domain.Recipe;
import com.springframework.repositories.reactive.IngredientReactiveRepository;
import com.springframework.repositories.reactive.RecipeReactiveRepository;
import com.springframework.repositories.reactive.UnitOfMeasureReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Disabled("For Mongo things should be different")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class IngredientServiceIT {
    @Autowired
    RecipeReactiveRepository recipeRepo;

    @Autowired
    IngredientReactiveRepository ingredientRepo;

    @Autowired
    UnitOfMeasureReactiveRepository uomRepo;

    IngredientService service;

    IngredientToCommand ingConverter;
    CommandToIngredient toIngConverter;
    UnitOfMeasureToCommand uomConverter;
    CommandToUnitOfMeasure toUomConverter;

    @BeforeEach
    void setUp() {
        uomConverter = new UnitOfMeasureToCommand();
        ingConverter = new IngredientToCommand(uomConverter);
        toUomConverter = new CommandToUnitOfMeasure();
        toIngConverter = new CommandToIngredient(toUomConverter);

        service = new IngredientServiceImpl(recipeRepo, ingredientRepo,
                                    ingConverter,uomRepo,toIngConverter);
    }

    @Test
    void findByRecipeIdAndIngredientId() {
        Recipe recipe = new Recipe();
        recipe.addIngredient(new Ingredient());
        Recipe savedRecipe = recipeRepo.save(recipe).block();

        assertNotNull(savedRecipe);

        String recipeId = savedRecipe.getId();
        String ingrId = savedRecipe.getIngredients().iterator().next().getId();

        IngredientCommand command = service.findByRecipeIdAndIngredientId(recipeId,ingrId);

        System.out.println("recipe Id: " + recipeId);
        System.out.println("ingred Id: " + ingrId);

        assertNotNull(command);
        assertEquals(ingrId, command.getId());
        assertEquals(recipeId, command.getRecipeId());
    }

}