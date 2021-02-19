package com.springframework.converters;

import com.springframework.commands.IngredientCommand;
import com.springframework.domain.Ingredient;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class CommandToIngredient implements Converter<IngredientCommand, Ingredient> {

    CommandToUnitOfMeasure converter;
    public CommandToIngredient(CommandToUnitOfMeasure converter) {
        this.converter = converter;
    }

    @Synchronized
    @Nullable
    @Override
    public Ingredient convert(IngredientCommand source) {
        if (source == null) {
            return null;
        }

        final Ingredient ingredient = new Ingredient();
        ingredient.setId(source.getId());
        ingredient.setDescription(source.getDescription());
        ingredient.setAmount(source.getAmount());
        ingredient.setUom(converter.convert(source.getUom()));
        //to figure out if line below is needed
        //ingredient.getRecipe().setId(source.getRecipeId());
        return ingredient;
    }
}
