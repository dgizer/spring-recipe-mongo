package com.springframework.services;

import com.springframework.commands.RecipeCommand;
import com.springframework.converters.CommandToRecipe;
import com.springframework.converters.RecipeToCommand;
import com.springframework.domain.Recipe;
import com.springframework.repositories.reactive.RecipeReactiveRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@Disabled
@ExtendWith(SpringExtension.class)
@SpringBootTest
class RecipeServiceIT {
    private static final String NEW_DESCRIPTION = "New description";


    @Autowired
    RecipeService service;

    @Autowired
    RecipeReactiveRepository repository;

    @Autowired
    CommandToRecipe commandToRecipe;

    @Autowired
    RecipeToCommand recipeToCommand;


    @Test
    void saveRecipeCommand() {
        //given

        Iterable<Recipe> recipes = repository.findAll().toIterable();
        System.out.println("recipes size: " + repository.count().block());
        Recipe testRecipe = recipes.iterator().next();
        RecipeCommand testComand = recipeToCommand.convert(testRecipe);

        //when
        testComand.setDescription(NEW_DESCRIPTION);
        RecipeCommand savedComand = service.saveRecipeCommand(testComand).block();

        //then
        assertEquals(NEW_DESCRIPTION, savedComand.getDescription());
        assertEquals(testRecipe.getId(), savedComand.getId());
        assertEquals(testRecipe.getCategories().size(), savedComand.getCategories().size());
        assertEquals(testRecipe.getIngredients().size(), savedComand.getIngredients().size());
    }


}