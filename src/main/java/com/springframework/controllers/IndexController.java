package com.springframework.controllers;

import com.springframework.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class IndexController {

    private final RecipeService recipeService;

    public IndexController(RecipeService recipeService) {
        log.debug("Injecting the service");
        this.recipeService = recipeService;
    }

    @GetMapping({"","/", "/index","/index.html"})
    public String getIndexPage(Model model){
        log.debug("Displaying Index page");
        model.addAttribute("recipes", recipeService.getRecipes());
        return "index";
    }
}
