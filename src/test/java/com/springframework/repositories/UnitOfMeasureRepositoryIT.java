package com.springframework.repositories;

import com.springframework.bootstrap.RecipeBootstrap;
import com.springframework.domain.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataMongoTest
class UnitOfMeasureRepositoryIT {


    @Autowired
    private UnitOfMeasureRepository unitOfMeasureRepository;

    @Autowired
    RecipeRepository recipeRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        recipeRepository.deleteAll();
        categoryRepository.deleteAll();
        unitOfMeasureRepository.deleteAll();

        RecipeBootstrap bootstrap = new RecipeBootstrap(recipeRepository,categoryRepository,unitOfMeasureRepository);
        bootstrap.onApplicationEvent(null);
    }

    @Test
    void findByDescription() {
        String strQuery = "Teaspoon";
        Optional<UnitOfMeasure> uomOpt = unitOfMeasureRepository.findByDescription(strQuery);

        assertTrue(uomOpt.isPresent());
        assertEquals(strQuery, uomOpt.get().getDescription());

    }

    @Test
    void findByDescriptionCup() {
        String strQuery = "Cup";
        Optional<UnitOfMeasure> uomOpt = unitOfMeasureRepository.findByDescription(strQuery);

        assertEquals(strQuery,uomOpt.get().getDescription());

    }
}