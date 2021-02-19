package com.springframework.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@EqualsAndHashCode(exclude = {"recipes"})
@NoArgsConstructor
public class Category {

    @Builder
    public Category(String id, String description, Set<Recipe> recipes) {
        this.id = id;
        this.description = description;
        this.recipes = recipes;
    }

    private String id;
    private String description;
    private Set<Recipe> recipes;
}
