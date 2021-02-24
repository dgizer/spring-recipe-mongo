package com.springframework.controllers;

import com.springframework.commands.IngredientCommand;
import com.springframework.commands.RecipeCommand;
import com.springframework.commands.UnitOfMeasureCommand;
import com.springframework.services.IngredientService;
import com.springframework.services.RecipeService;
import com.springframework.services.UnitOfMeasureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class IngredientControllerTest {

    @Mock
    private RecipeService recipeService;
    @Mock
    private IngredientService ingredientService;
    @Mock
    private UnitOfMeasureService uomService;

    IngredientController controller;

    MockMvc mockMvc;


    @BeforeEach
    void setUp() {
        controller = new IngredientController(recipeService, ingredientService, uomService);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void showIngredients() throws Exception {
        //given
        String id1 = "1L";
        RecipeCommand command = new RecipeCommand();
        command.setId(id1);
        when(recipeService.findCommandById(anyString())).thenReturn(command);

        //when
        mockMvc.perform(get("/recipe/"+id1+"/ingredients"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredients/list"))
                .andExpect(model().attributeExists("recipe"));
        //then
        verify(recipeService).findCommandById(anyString());
    }

    @Test
    void showIngredient() throws Exception {
        //given
        String id = "1L";
        String recId = "2L";
        IngredientCommand command = new IngredientCommand();
        command.setId(id);
        command.setRecipeId(recId);

        //when
        when(ingredientService.findByRecipeIdAndIngredientId(anyString(), anyString())).thenReturn(command);

        //then
        mockMvc.perform(get("/recipe/1/ingredient/2/show"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredients/show"))
                .andExpect(model().attributeExists("ingredient"));
    }

    @Test
    void updateIngredient() throws Exception {
        //given
        IngredientCommand command = new IngredientCommand();
        UnitOfMeasureCommand uom1 = new UnitOfMeasureCommand();
        UnitOfMeasureCommand uom2 = new UnitOfMeasureCommand();
        Set<UnitOfMeasureCommand> uomSet = new HashSet<>();
        Collections.addAll(uomSet,uom1,uom2);

        //when
        when(ingredientService.findByRecipeIdAndIngredientId(anyString(), anyString())).thenReturn(command);
        when(uomService.listAllUoms()).thenReturn(Flux.just(uom1, uom2));

        //then
        mockMvc.perform(get("/recipe/1/ingredient/2/update"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredients/ingredientform"))
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(model().attributeExists("uomList"));
        verify(ingredientService).findByRecipeIdAndIngredientId(anyString(),anyString());
        verify(uomService).listAllUoms();
    }

    @Test
    void saveIngredient() throws Exception {
        //given
        String id = "1L";
        String recId = "2L";
        IngredientCommand command = new IngredientCommand();
        command.setId(id);
        command.setRecipeId(recId);

        //when
        when(ingredientService.saveIngredient(any())).thenReturn(command);

        //then
        mockMvc.perform(post("/recipe/"+recId+"/ingredient/save")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id","")
                .param("description", "some description")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/"+recId+"/ingredient/"+id+"/show"));

        verify(ingredientService).saveIngredient(any(IngredientCommand.class));
    }

    @Test
    void addIngredient() throws Exception {

        RecipeCommand command = new RecipeCommand();
        command.setId("1L");

        UnitOfMeasureCommand uom1 = new UnitOfMeasureCommand();
        uom1.setId("2L");
        UnitOfMeasureCommand uom2 = new UnitOfMeasureCommand();
        uom2.setId("3L");
        Set<UnitOfMeasureCommand> uomSet = new HashSet<>();
        Collections.addAll(uomSet, uom1, uom2);

        //when(recipeService.findById(anyLong())).thenReturn(command);
        when(uomService.listAllUoms()).thenReturn(Flux.just(uom1, uom2));

        mockMvc.perform(get("/recipe/1/ingredient/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("recipe/ingredients/ingredientform"))
                .andExpect(model().attributeExists("ingredient"))
                .andExpect(model().attributeExists("uomList"));
        verify(uomService).listAllUoms();
        //verify(recipeService).findById(anyLong());
    }

    @Test
    void deleteIngredient() throws Exception {
        String recId = "1L";
        String id = "2L";

        mockMvc.perform(get("/recipe/"+recId+"/ingredient/"+id+"/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/recipe/"+recId+"/ingredients"));
        verify(ingredientService).deleteIngredientById(anyString(),anyString());
    }
}