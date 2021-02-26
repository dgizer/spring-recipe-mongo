package com.springframework.controllers;

import com.springframework.commands.RecipeCommand;
import com.springframework.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
public class RecipeController {
    private static final String RECIPE_RECIPEFORM = "recipe/recipeform";
    private static final String RECIPE_SHOW = "recipe/show";

    RecipeService recipeService;
    private WebDataBinder webDataBinder;

    @InitBinder
    public void initBinder(WebDataBinder webDataBinder){
        this.webDataBinder = webDataBinder;
    }

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/recipe/{id}/show")
    public String showById(@PathVariable String id, Model model){
        model.addAttribute("recipe",recipeService.findById(id));
        return RECIPE_SHOW;
    }

    @GetMapping("/recipe/new")
    public String newRecipe(Model model){
        model.addAttribute("recipe", new RecipeCommand());
        return RECIPE_RECIPEFORM;
    }

    @GetMapping("/recipe/{id}/update")
    public String updateRecipe(@PathVariable String id, Model model) {
        model.addAttribute("recipe",recipeService.findCommandById(id));
        return RECIPE_RECIPEFORM;
    }

    @PostMapping("/recipe")
    public String saveOrUpdate(@ModelAttribute("recipe") RecipeCommand command){

        webDataBinder.validate();
        BindingResult bindingResult = webDataBinder.getBindingResult();
        if (bindingResult.hasErrors()){
            bindingResult.getAllErrors().forEach(objectError ->
                    log.debug(objectError.toString()));
            return RECIPE_RECIPEFORM;
        }
        System.out.println("ct1: " + command.getId());
        RecipeCommand savedCommand = recipeService.saveRecipeCommand(command).block();
        return "redirect:/recipe/" + savedCommand.getId() + "/show";


    }

    @GetMapping("/recipe/{id}/delete")
    public String deleteRecipeById(@PathVariable String id) {
        log.debug("deleting recipe with id:" + id);
        recipeService.deleteById(id);
        return "redirect:/";
    }

/*    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFound(Exception exception) {
        log.error("Not found exception thrown");
        log.error(exception.getMessage());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("404error");
        modelAndView.addObject("exception", exception);
        return modelAndView;
    }
*/

}
