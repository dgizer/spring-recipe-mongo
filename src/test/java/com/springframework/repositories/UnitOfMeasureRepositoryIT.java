package com.springframework.repositories;

import com.springframework.domain.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Disabled("For Mongo DB the things for IT should be fixed")
@ExtendWith(SpringExtension.class)
//@DataJpaTest
class UnitOfMeasureRepositoryIT {

    @Autowired
    private UnitOfMeasureRepository unitOfMeasureRepository;

    @BeforeEach
    void setUp() {
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