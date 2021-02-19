package com.springframework.services;

import com.springframework.commands.RecipeCommand;
import com.springframework.converters.CommandToRecipe;
import com.springframework.converters.RecipeToCommand;
import com.springframework.domain.Recipe;
import com.springframework.exceptions.NotFoundException;
import com.springframework.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final CommandToRecipe commandToRecipe;
    private final RecipeToCommand recipeToCommand;

    public RecipeServiceImpl(RecipeRepository recipeRepository, CommandToRecipe commandToRecipe, RecipeToCommand recipeToCommand) {
        this.recipeRepository = recipeRepository;
        this.commandToRecipe = commandToRecipe;
        this.recipeToCommand = recipeToCommand;
    }

    @Override
    public Set<Recipe> getRecipes() {
        log.debug("Start getRecipe method...");
        Set<Recipe> recipes = new HashSet<>();
        recipeRepository.findAll().iterator().forEachRemaining(recipes::add);
        return recipes;
    }

    @Override
    public Recipe findById(String id) {
        Optional<Recipe> recipeOpt = recipeRepository.findById(id);
        if (!recipeOpt.isPresent()) {
            throw new NotFoundException("Recipe is not present! ID value " + id + " is missed");
        }
        return recipeOpt.get();
    }

    @Override
    @Transactional
    public RecipeCommand findCommandById(String id) {
        return recipeToCommand.convert(recipeRepository.findById(id).get());
    }

    @Override
    @Transactional
    public RecipeCommand saveRecipeCommand(RecipeCommand command) {
        Recipe detachedRecipe = commandToRecipe.convert(command);

        Recipe savedRecipe = recipeRepository.save(detachedRecipe);
        log.debug("Saved Recipe with id: " + savedRecipe.getId());
        return recipeToCommand.convert(savedRecipe);
    }

    @Override
    public void deleteById(String id) {
        recipeRepository.deleteById(id);
    }
}
