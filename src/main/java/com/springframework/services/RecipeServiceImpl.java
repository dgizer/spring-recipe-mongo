package com.springframework.services;

import com.springframework.commands.RecipeCommand;
import com.springframework.converters.CommandToRecipe;
import com.springframework.converters.RecipeToCommand;
import com.springframework.domain.Recipe;
import com.springframework.exceptions.NotFoundException;
import com.springframework.repositories.reactive.RecipeReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeReactiveRepository recipeRepository;
    private final CommandToRecipe commandToRecipe;
    private final RecipeToCommand recipeToCommand;

    public RecipeServiceImpl(RecipeReactiveRepository recipeRepository, CommandToRecipe commandToRecipe, RecipeToCommand recipeToCommand) {
        this.recipeRepository = recipeRepository;
        this.commandToRecipe = commandToRecipe;
        this.recipeToCommand = recipeToCommand;
    }

    @Override
    public Flux<Recipe> getRecipes() {
        log.debug("Start getRecipe method...");
        return recipeRepository.findAll();
    }

    @Override
    public Mono<Recipe> findById(String id) {
        return recipeRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Recipe is not present! ID value " + id + " is missed")));
    }

    @Override
    public Mono<RecipeCommand> findCommandById(String id) {
        return recipeRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Recipe is not present! ID value " + id + " is missed")))
                .map(recipeToCommand::convert);

        /*return recipeRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Recipe is not present! ID value " + id + " is missed")))
                .map(recipe -> {
                    RecipeCommand command = recipeToCommand.convert(recipe);
                    command.getIngredients().forEach(ing -> {
                        ing.setRecipeId(command.getId());
                    });
                    return command;
                });*/
    }

    @Override
    public Mono<RecipeCommand> saveRecipeCommand(RecipeCommand command) {
        return  recipeRepository.save(commandToRecipe.convert(command))
                .map(recipeToCommand::convert);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        recipeRepository.deleteById(id).block();
        return Mono.empty();
    }
}
