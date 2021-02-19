package com.springframework.converters;

import com.springframework.commands.IngredientCommand;
import com.springframework.domain.Ingredient;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

@Component
public class IngredientToCommand implements Converter<Ingredient, IngredientCommand> {
    UnitOfMeasureToCommand converter;
    public IngredientToCommand(UnitOfMeasureToCommand converter) {
        this.converter = converter;
    }

    @Synchronized
    @Nullable
    @Override
    public IngredientCommand convert(Ingredient source) {
        if (source == null) {
            return null;
        }
        final IngredientCommand command = new IngredientCommand();
        command.setId(source.getId());
        if (source.getRecipe() != null) {
            command.setRecipeId(source.getRecipe().getId());
        }
        command.setDescription(source.getDescription());
        command.setAmount(source.getAmount());
        command.setUom(converter.convert(source.getUom()));
        return command;
    }
}
