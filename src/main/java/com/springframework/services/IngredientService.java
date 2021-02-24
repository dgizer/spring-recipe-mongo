package com.springframework.services;

import com.springframework.commands.IngredientCommand;
import reactor.core.publisher.Mono;

public interface IngredientService {
    Mono<IngredientCommand> findByRecipeIdAndIngredientId(String recipeId, String ingredientId);
    Mono<IngredientCommand> saveIngredient(IngredientCommand command);
    Mono<Void> deleteIngredientById(String recId, String id);
}
