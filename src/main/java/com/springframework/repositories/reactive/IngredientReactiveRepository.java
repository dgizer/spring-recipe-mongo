package com.springframework.repositories.reactive;

import com.springframework.domain.Ingredient;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface IngredientReactiveRepository extends ReactiveMongoRepository<Ingredient, String> {
}
