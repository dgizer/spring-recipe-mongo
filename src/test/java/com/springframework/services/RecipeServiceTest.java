package com.springframework.services;

import com.springframework.commands.RecipeCommand;
import com.springframework.converters.CommandToRecipe;
import com.springframework.converters.RecipeToCommand;
import com.springframework.domain.Recipe;
import com.springframework.exceptions.NotFoundException;
import com.springframework.repositories.reactive.RecipeReactiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {

    private  RecipeService recipeService;

    @Mock
    private RecipeReactiveRepository recipeRepository;


    @Mock
    private CommandToRecipe commandToRecipe;

    @Mock
    private RecipeToCommand recipeToCommand;

    @BeforeEach
    void setUp() {
        recipeService = new RecipeServiceImpl(recipeRepository, commandToRecipe, recipeToCommand);
    }

    @Test
    void getRecipesById() {
        Recipe recipe = new Recipe();
        recipe.setId("1L");

        when(recipeRepository.findById(anyString())).thenReturn(Mono.just(recipe));

        Recipe recipeReturned = recipeService.findById("1L").block();

        assertNotNull(recipeReturned,"Null recipe returned");
        verify(recipeRepository, times(1)).findById(anyString());
        verify(recipeRepository, never()).findAll();
    }


    @Test
    void getRecipes() {
        Recipe recipe = new Recipe();
        //Set<Recipe> recipesData = new HashSet<>();
        List<Recipe> recipesData = new ArrayList<>();
        recipesData.add(recipe);

        when(recipeRepository.findAll()).thenReturn(Flux.fromIterable(recipesData));

        List<Recipe> recipes = recipeService.getRecipes().collectList().block();
        assertEquals(recipes.size(), recipesData.size());

        verify(recipeRepository, times(1)).findAll();
        verify(recipeRepository, never()).findById(anyString());
    }

    @Test
    void findCommandById() {
        String id = "2L";

        Recipe recipe = new Recipe();
        recipe.setId(id);

        when(recipeRepository.findById(anyString())).thenReturn(Mono.just(recipe));

        assertNotNull(recipeRepository.findById(id));
        verify(recipeRepository).findById(anyString());

        RecipeCommand command = new RecipeCommand();
        command.setId(id);

        when(recipeToCommand.convert(any())).thenReturn(command);

        RecipeCommand findRec = recipeService.findCommandById(id).block();

        assertNotNull(findRec,"Null is returned");
        verify(recipeToCommand).convert(any());
    }

    @Test
    void deleteById() {
        //given
        String id = "5L";
        //Recipe testRecipe = new Recipe();
        //testRecipe.setId(id);
        //recipeRepository.save(testRecipe).block();
        when(recipeRepository.deleteById(anyString())).thenReturn(Mono.empty());

        //when
        recipeService.deleteById(id).block();

        //then
        verify(recipeRepository).deleteById(anyString());
    }

    @Test
    void getRecipeByIdTestNotFound() throws Exception {

        //Optional<Recipe> recipeOpt = Optional.empty();
        when(recipeRepository.findById(anyString())).thenReturn(Mono.empty());

        //Recipe recipeReturned = recipeService.findById(1L);
        Exception exception = assertThrows(NotFoundException.class, () -> recipeService.findById("1l").block());
    }
}