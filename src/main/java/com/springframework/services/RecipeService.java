package com.springframework.services;

import com.springframework.commands.RecipeCommand;
import com.springframework.domain.Recipe;

import java.util.Set;

public interface RecipeService {
     Set<Recipe> getRecipes();
     Recipe findById(String id);
     RecipeCommand saveRecipeCommand(RecipeCommand command);
     RecipeCommand findCommandById(String id);
     void deleteById(String id);
}
