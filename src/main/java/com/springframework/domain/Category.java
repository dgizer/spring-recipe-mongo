package com.springframework.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Document
public class Category {

    @Builder
    public Category(String id, String description, Set<Recipe> recipes) {
        this.id = id;
        this.description = description;
        this.recipes = recipes;
    }

    @Id
    private String id;
    private String description;
    private Set<Recipe> recipes;
}
