package com.springframework.controllers;

import com.springframework.domain.Recipe;
import com.springframework.exceptions.NotFoundException;
import com.springframework.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@WebFluxTest(RecipeController.class)
class RecipeControllerTest {

    @Autowired
    WebTestClient webClient;

    @MockBean
    RecipeService recipeService;

    RecipeController recipeController;

    @BeforeEach
    void setUp() {
        recipeController = new RecipeController(recipeService);
    }

    @Test
    public void testGetRecipe() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId("1L");

        when(recipeService.findById(anyString())).thenReturn(Mono.just(recipe));

        webClient.get()
                .uri("/recipe/1/show")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody();


    }

    @Test
    void testModelGetRecipe() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setId("1L");

        when(recipeService.findById(anyString())).thenReturn(Mono.just(recipe));

        EntityExchangeResult<byte[]> result = this.webClient.get()
                .uri("/recipe/1/show")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .returnResult();

/*        MockMvcWebTestClient.resultActionsFor(result)
                .andExpect(model().attributeExists("recipe"))
                .andExpect(view().name("recipe/show"));*/
    }

    @Test
    void testGetRecipeNotFound() throws Exception {

        when(recipeService.findById(anyString())).thenThrow(NotFoundException.class);

        webClient.get()
                .uri("/recipe/1/show")
                .exchange()
                .expectStatus()
                .isNotFound();

    }

    //todo write test for non-valid id in path
/*
    @Test
    void testGetRecipeNumberFormatWrong() throws Exception {
        mockMvc.perform(get("/recipe/asd/show"))
                .andExpect(status().isBadRequest())
                .andExpect(view().name("400error"));
    }
*/


    /*
    @Test
    public void testGetNewRecipe() throws Exception {
        mockMvc.perform(get("/recipe/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipeform"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    public void testPostToRecipe() throws Exception {
        RecipeCommand command = new RecipeCommand();
        command.setId("2L");

        when(recipeService.saveRecipeCommand(any())).thenReturn(Mono.just(command));

        mockMvc.perform(post("/recipe")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id","")
                .param("description", "some descriptions")
                .param("prepTime","10")
                .param("directions", "some directions")
        )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/" + command.getId() + "/show"));

    }


    @Test
    public void testPostToRecipeFailingValidation() throws Exception {
        RecipeCommand command = new RecipeCommand();
        command.setId("2L");

//        when(recipeService.saveRecipeCommand(any())).thenReturn(command);

        mockMvc.perform(post("/recipe")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id","")

        )
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("recipe"))
                .andExpect(view().name("recipe/recipeform"));

    }

    @Test
    public void  testUpdateRecipe() throws Exception {
        RecipeCommand command = new RecipeCommand();
        command.setId("5L");

        when(recipeService.findCommandById(anyString())).thenReturn(Mono.just(command));

        mockMvc.perform(get("/recipe/"+command.getId()+"/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/recipeform"))
                .andExpect(model().attributeExists("recipe"));
    }

    @Test
    void testDeleteRecipeById() throws Exception {
        String id = "1L";

        mockMvc.perform(get("/recipe/"+id +"/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/"));
        verify(recipeService).deleteById(anyString());
    }

     */
}