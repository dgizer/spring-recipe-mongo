package com.springframework.domain;

import com.springframework.enums.Difficulty;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class Recipe {
    private String id;

    private String description;
    private Integer prepTime;
    private Integer cookTime;
    private Integer servings;
    private String source;
    private String url;
    private String directions;
    private Set<Ingredient> ingredients = new HashSet<>();
    private Byte[] image;
    private Difficulty difficulty;
    private Notes notes;
    private Set<Category> categories = new HashSet<>();


    public void setNotes(Notes notes) {
            this.notes = notes;
        if (notes != null) {
            notes.setRecipe(this);
        }
    }

    public Recipe addIngredient(Ingredient ingredient) {
        this.ingredients.add(ingredient);
        ingredient.setRecipe(this);
        return this;
    }
}
