package com.springframework.domain;

import lombok.*;

import java.util.Set;

@Getter
@Setter
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
