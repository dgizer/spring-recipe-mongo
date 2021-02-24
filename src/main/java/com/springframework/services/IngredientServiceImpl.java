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
    public Mono<IngredientCommand> findByRecipeIdAndIngredientId(String recipeId, String ingredientId) {
        return recipeRepository.findById(recipeId)
                .flatMapIterable(Recipe::getIngredients)
                .filter(ingredient -> ingredient.getId().equalsIgnoreCase(ingredientId))
                .single()
                .map(ingredient -> {
                    IngredientCommand command = ingredientToCommand.convert(ingredient);
                    command.setRecipeId(recipeId);
                    return command;
                });
    }

    @Override
    @Transactional
    public Mono<IngredientCommand> saveIngredient(IngredientCommand command){
        Recipe recipe = recipeRepository.findById(command.getRecipeId()).block();
        boolean ingrIsNew = false;

        if (recipe == null) {
            //todo handle appropriately the error
            log.debug("Recipe with given id not found: id: " + command.getRecipeId());
        } else {
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

                if (foundIngr.getUom() == null){
                    throw new RuntimeException("Uom not found");
                }
                //.orElseThrow(() -> new RuntimeException("Unit of measure not found"))); //todo appropriate exception handler
            } else {
                //add new Ingredient
                ingrIsNew = true;
                recipe.addIngredient(commandToIngredient.convert(command));

            }
            Recipe recipeSaved = recipeRepository.save(recipe).block();

            if (ingrIsNew){

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
            return Mono.just(savedCommand);
        }
        return null;
    }    //todo test saveIngredient() by integration test for testing properly the id assignment to new ingredient
         //todo add test for existing ingredient


    @Override
    @Transactional
    public Mono<Void> deleteIngredientById(String recid, String id) {
        Recipe recipe = recipeRepository.findById(recid).block();
        //Optional<Recipe> recipeOpt = recipeSimpleRepo.findById(recid);

        if(recipe != null) {
            recipe.getIngredients()
                    .removeIf(ingredient -> ingredient.getId().equals(id));
            recipeRepository.save(recipe).block();

            //todo: Check if now it is still required (in sql it was necessary to remove ingredient from DB)
            ingredientRepository.deleteById(id).block();
        } else {
            log.debug("raise an exception");
        }
        return Mono.empty();
    }
}

