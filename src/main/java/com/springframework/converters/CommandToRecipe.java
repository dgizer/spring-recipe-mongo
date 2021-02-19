package com.springframework.converters;

import com.springframework.commands.RecipeCommand;
import com.springframework.domain.Recipe;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class CommandToRecipe implements Converter<RecipeCommand, Recipe> {
    CommandToIngredient toIngrConverter;
    CommandToNotes toNotesConverter;
    CommandToCategory toCategConverter;

    public CommandToRecipe(CommandToIngredient toIngrConverter,
                           CommandToNotes toNotesConverter,
                           CommandToCategory toCategConverter) {
        this.toIngrConverter = toIngrConverter;
        this.toNotesConverter = toNotesConverter;
        this.toCategConverter = toCategConverter;
    }

    @Synchronized
    @Nullable
    @Override
    public Recipe convert(RecipeCommand source) {
        if (source == null){
            return null;
        }

        final Recipe recipe = new Recipe();
        recipe.setId(source.getId());
        recipe.setDescription(source.getDescription());
        recipe.setPrepTime(source.getPrepTime());
        recipe.setCookTime(source.getCookTime());
        recipe.setServings(source.getServings());
        recipe.setSource(source.getSource());
        recipe.setUrl(source.getUrl());
        recipe.setDirections(source.getDirections());
        recipe.setIngredients(source.getIngredients().stream().map(toIngrConverter::convert).collect(Collectors.toSet()));
        recipe.setDifficulty(source.getDifficulty());
        recipe.setNotes(toNotesConverter.convert(source.getNotes()));
        recipe.setCategories(source.getCategories().stream().map(toCategConverter::convert).collect(Collectors.toSet()));
        return recipe;
    }
}
