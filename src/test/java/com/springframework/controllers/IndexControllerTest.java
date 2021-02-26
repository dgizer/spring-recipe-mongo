package com.springframework.controllers;

import com.springframework.domain.Recipe;
import com.springframework.services.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.ui.Model;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@WebFluxTest(IndexController.class)
class IndexControllerTest {

    private IndexController controller;

    @MockBean
    private RecipeService recipeService;

    @MockBean
    private Model model;

    @Autowired
    WebTestClient webClient;

    @BeforeEach
    void setUp() {
        controller = new IndexController(recipeService);
        //webClient = WebTestClient.bindToController(controller).build();
    }

    @Test
    void testWebClient() throws Exception {
        when(recipeService.getRecipes()).thenReturn(Flux.empty());

        webClient.get().uri("/")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody();
    }

    @Test
    void getIndexPage() {
        //given
        List<Recipe> recipes = new ArrayList<>();

        Recipe recipe1 = new Recipe();
        recipe1.setId("1L");
        Recipe recipe2 = new Recipe();
        recipe1.setId("2L");
        Collections.addAll(recipes, recipe1,recipe2);

        when(recipeService.getRecipes()).thenReturn(Flux.fromIterable(recipes));

        ArgumentCaptor<Flux<Recipe>> argumentCaptor = ArgumentCaptor.forClass(Flux.class);

        //when
        String view = controller.getIndexPage(model);

        //then
        assertEquals(view,"index");
        verify(recipeService, times(1)).getRecipes();
        verify(model, times(1)).addAttribute(eq("recipes"),argumentCaptor.capture());

        List<Recipe> setInController = argumentCaptor.getValue().collectList().block();
        assertEquals(2, setInController.size());
    }
}