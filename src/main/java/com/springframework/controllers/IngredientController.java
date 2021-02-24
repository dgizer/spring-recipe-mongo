package com.springframework.controllers;

import com.springframework.commands.IngredientCommand;
import com.springframework.commands.RecipeCommand;
import com.springframework.commands.UnitOfMeasureCommand;
import com.springframework.services.IngredientService;
import com.springframework.services.RecipeService;
import com.springframework.services.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class IngredientController {
    private final RecipeService recipeService;
    private final IngredientService ingredientService;
    private final UnitOfMeasureService uomService;

    public IngredientController(RecipeService recipeService, IngredientService ingredientService,
                                UnitOfMeasureService uomService) {
        this.recipeService = recipeService;
        this.ingredientService = ingredientService;
        this.uomService = uomService;
    }

    @GetMapping("/recipe/{recipeId}/ingredients")
    public String showIngredients(@PathVariable String recipeId, Model model) {
        log.debug("Getting ingredients for Recipe id:" + recipeId);
        model.addAttribute("recipe",recipeService.findCommandById(recipeId));
        return "recipe/ingredients/list";
    }

    @GetMapping("recipe/{recipeId}/ingredient/{id}/show")
    public String showIngredient(@PathVariable String recipeId, @PathVariable String id, Model model){
        log.debug("getting particluar ingredient with id: " + id+", of Recipe with id: "+recipeId);
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipeId,id).block());
        return "recipe/ingredients/show";
    }

    @GetMapping("recipe/{recipeId}/ingredient/{id}/update")
    public String updateIngredient(@PathVariable String recipeId, @PathVariable String id, Model model){
        log.debug("getting particluar ingredient with id: " + id+", of Recipe with id: "+recipeId);
        model.addAttribute("ingredient", ingredientService.findByRecipeIdAndIngredientId(recipeId, id).block());
        log.debug("get the list of uoms");
        model.addAttribute("uomList",uomService.listAllUoms().collectList().block());
        return "recipe/ingredients/ingredientform";
    }

    //TODO IMPORTANT!!! Saving new ingredient is not wworking due to issues of ids (now there are no relations
    // between recipe and ingredient via ID)
    @PostMapping("recipe/{recipeId}/ingredient/save")
    public String saveIngredient(@ModelAttribute IngredientCommand command) {
        IngredientCommand savedCommand = ingredientService.saveIngredient(command).block();
        log.debug("saved ingredient id: " + savedCommand.getId());
        log.debug("saved recipe id: " + savedCommand.getRecipeId());

        return "redirect:/recipe/" + savedCommand.getRecipeId() + "/ingredient/" + savedCommand.getId()+"/show";
    }

    @GetMapping("recipe/{recipeId}/ingredient/new")
    public String addIngredient(@PathVariable String recipeId, Model model) {
        RecipeCommand recipeCommand = recipeService.findCommandById(recipeId);
        //todo handle error throwing properly
        if (recipeCommand == null)
            log.error("No recipe with id: " + recipeId);
            //throw new RuntimeException("No recipe with id: " + recipeId);

        IngredientCommand newIngr = new IngredientCommand();
        model.addAttribute("ingredient", newIngr);
        log.debug("new ingredient created for recipe id:" + recipeId);
        //init uom
        newIngr.setUom(new UnitOfMeasureCommand());
        newIngr.setRecipeId(recipeId);

        model.addAttribute("uomList", uomService.listAllUoms().collectList().block());
        log.debug("got list of uoms");

        return "recipe/ingredients/ingredientform";
    }

    @GetMapping("recipe/{recId}/ingredient/{id}/delete")
    public String deleteIngredient(@PathVariable String recId,
                                   @PathVariable String id) {
        ingredientService.deleteIngredientById(recId,id).block();
        log.debug("deleting ingredient id: "+id);
        return "redirect:/recipe/"+recId+"/ingredients";
    }
}

