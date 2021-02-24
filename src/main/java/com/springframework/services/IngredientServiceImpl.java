package com.springframework.services;

import com.springframework.commands.IngredientCommand;
import com.springframework.converters.CommandToIngredient;
import com.springframework.converters.IngredientToCommand;
import com.springframework.domain.Ingredient;
import com.springframework.domain.Recipe;
import com.springframework.repositories.reactive.IngredientReactiveRepository;
import com.springframework.repositories.reactive.RecipeReactiveRepository;
import com.springframework.repositories.reactive.UnitOfMeasureReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {
    private final RecipeReactiveRepository recipeRepository;
    private final IngredientReactiveRepository ingredientRepository;
    private final IngredientToCommand ingredientToCommand;
    private final UnitOfMeasureReactiveRepository uomRepo;
    private final CommandToIngredient commandToIngredient;

    public IngredientServiceImpl(RecipeReactiveRepository recipeRepository,
                                 IngredientReactiveRepository ingredientRepository,
                                 IngredientToCommand ingredientToCommand,
                                 UnitOfMeasureReactiveRepository uomRepo,
                                 CommandToIngredient commandToIngredient) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.ingredientToCommand = ingredientToCommand;
        this.uomRepo = uomRepo;
        this.commandToIngredient = commandToIngredient;
    }

    @Override
    public IngredientCommand findByRecipeIdAndIngredientId(String recipeId, String ingredientId) {
        Mono<Recipe> recipeOpt = recipeRepository.findById(recipeId);
        /*if (recipeOpt.block() == null){
            //todo implementation error handling
            log.debug("recipe id is not found: " + recipeId);
        }*/
        Recipe recipe = recipeOpt.block();
        Optional<IngredientCommand> commandOpt = recipe.getIngredients().stream()
                .filter(ingredient -> ingredient.getId().equals(ingredientId))
                .map(ingredientToCommand::convert).findFirst();
        if (!commandOpt.isPresent()) {
            //todo implement error handler
            log.debug("Ingredient id is not found: " + ingredientId);
        }
        IngredientCommand command = commandOpt.get();
        command.setRecipeId(recipeId);
        return command;
    }

    @Override
    @Transactional
    public IngredientCommand saveIngredient(IngredientCommand command){
        Mono<Recipe> recipeOpt = recipeRepository.findById(command.getRecipeId());
        boolean ingrIsNew = false;

        /*if (!recipeOpt.isPresent()) {
            //todo handle appropriately the error
            log.debug("Recipe with given id not found: id: " + command.getRecipeId());
        } else {*/
            Recipe recipe = recipeOpt.block();

            Optional<Ingredient> foundIngrOpt = recipe.getIngredients()
                    .stream()
                    .filter(ingredient -> ingredient.getId().equals(command.getId()))
                    .findFirst();

            if (foundIngrOpt.isPresent()) {
                Ingredient foundIngr = foundIngrOpt.get();
                foundIngr.setDescription(command.getDescription());
                foundIngr.setAmount(command.getAmount());
                foundIngr.setUom(uomRepo
                        .findById(command.getUom().getId()).block());
                        //.orElseThrow(() -> new RuntimeException("Unit of measure not found"))); //todo appropriate exception handler
            } else {
                //add new Ingredient
                ingrIsNew = true;
                recipe.addIngredient(commandToIngredient.convert(command));

            }
            Recipe recipeSaved = recipeRepository.save(recipe).block();

            if (ingrIsNew == true){

                Optional<Ingredient> commandId = recipeSaved.getIngredients().stream()
                        .filter(ingredient -> ingredient.getDescription().equals(command.getDescription()))
                        .filter(ingredient -> ingredient.getAmount().equals(command.getAmount()))
                        .filter(ingredient -> ingredient.getUom().getId().equals(command.getUom().getId()))
                        .findFirst();

                if(commandId.isPresent()) {
                    command.setId(commandId.get().getId());
                }
                else {
                    //todo handle exception
                    log.debug("error in adding new ingredient");
                }
            }

            IngredientCommand savedCommand = ingredientToCommand.convert(recipeSaved.getIngredients()
                    .stream()
                    .filter(recipeIngr -> recipeIngr.getId().equals(command.getId()))
                    .findFirst()
                    .get());
            savedCommand.setRecipeId(recipe.getId());

            //todo check if fails
            return savedCommand;
        //}
        //return null;
    }    //todo test saveIngredient() by integration test for testing properly the id assignment to new ingredient
         //todo add test for existing ingredient


    @Override
    @Transactional
    public void deleteIngredientById(String recid, String id) {
        Mono<Recipe> recipeOpt = recipeRepository.findById(recid);

        //if(recipeOpt.isPresent()) {
            recipeOpt.block().getIngredients()
                    .removeIf(ingredient -> ingredient.getId().equals(id));
            recipeRepository.save(recipeOpt.block());
            ingredientRepository.deleteById(id);
        /*} else {
            log.debug("raise an exception");
        }*/
    }
}

