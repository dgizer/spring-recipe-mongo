package com.springframework.converters;

import com.springframework.commands.RecipeCommand;
import com.springframework.domain.Recipe;
import lombok.Synchronized;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class RecipeToCommand implements Converter<Recipe, RecipeCommand> {
    IngredientToCommand ingrConverter;
    NotesToCommand notesConverter;
    CategoryToCommand catConverter;

    public RecipeToCommand(IngredientToCommand ingrConverter,
                           NotesToCommand notesConverter,
                           CategoryToCommand catConverter) {
        this.ingrConverter = ingrConverter;
        this.notesConverter = notesConverter;
        this.catConverter = catConverter;
    }

    @Synchronized
    @Nullable
    @Override
    public RecipeCommand convert(Recipe source) {
        if (source == null) {
            return null;
        }
        final RecipeCommand command = new RecipeCommand();
        command.setId(source.getId());
        command.setDescription(source.getDescription());
        command.setPrepTime(source.getPrepTime());
        command.setCookTime(source.getCookTime());
        command.setServings(source.getServings());
        command.setSource(source.getSource());
        command.setUrl(source.getUrl());
        command.setDirections(source.getDirections());
        command.setIngredients(source.getIngredients()
                .stream()
                .map(ingrConverter::convert)
                .collect(Collectors.toSet())
        );
        command.setImage(source.getImage());
        command.setDifficulty(source.getDifficulty());
        command.setNotes(notesConverter.convert(source.getNotes()));
        command.setCategories(source.getCategories()
                .stream()
                .map(catConverter::convert)
                .collect(Collectors.toSet())
        );
        return command;
    }
}
