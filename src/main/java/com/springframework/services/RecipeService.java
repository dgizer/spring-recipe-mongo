package com.springframework.services;

import com.springframework.commands.RecipeCommand;
import com.springframework.domain.Recipe;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RecipeService {
     Flux<Recipe> getRecipes();
     Mono<Recipe> findById(String id);
     Mono<RecipeCommand> saveRecipeCommand(RecipeCommand command);
     Mono<RecipeCommand> findCommandById(String id);
     Mono<Void> deleteById(String id);
}
