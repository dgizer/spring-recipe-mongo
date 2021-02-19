package com.springframework.services;

import com.springframework.commands.IngredientCommand;

public interface IngredientService {
    IngredientCommand findByRecipeIdAndIngredientId(String recipeId, String ingredientId);
    IngredientCommand saveIngredient(IngredientCommand command);
    void deleteIngredientById(String recId, String id);
}
