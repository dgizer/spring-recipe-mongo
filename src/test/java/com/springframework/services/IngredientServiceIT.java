package com.springframework.services;

import com.springframework.commands.IngredientCommand;
import com.springframework.converters.CommandToIngredient;
import com.springframework.converters.CommandToUnitOfMeasure;
import com.springframework.converters.IngredientToCommand;
import com.springframework.converters.UnitOfMeasureToCommand;
import com.springframework.repositories.IngredientRepository;
import com.springframework.repositories.RecipeRepository;
import com.springframework.repositories.UnitOfMeasureRepository;
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
    RecipeRepository recipeRepo;

    @Autowired
    IngredientRepository ingredientRepo;

    @Autowired
    UnitOfMeasureRepository uomRepo;

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


        IngredientCommand command = service.findByRecipeIdAndIngredientId("1L","2L");


        assertNotNull(command);
        assertEquals("2L", command.getId());
        assertEquals("1L", command.getRecipeId());
    }

}