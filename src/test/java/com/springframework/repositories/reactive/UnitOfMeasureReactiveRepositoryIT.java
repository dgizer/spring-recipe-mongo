package com.springframework.repositories.reactive;

import com.springframework.domain.UnitOfMeasure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataMongoTest
class UnitOfMeasureReactiveRepositoryIT {

    public static final String TEASPOON = "2 Teaspoon";
    @Autowired
    UnitOfMeasureReactiveRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll().block();
    }

    @Test
    void testSave() {
        UnitOfMeasure uom = new UnitOfMeasure();
        uom.setDescription(TEASPOON);

        UnitOfMeasure saved = repository.save(uom).block();

        assertEquals(1L, repository.count().block());
    }

    @Test
    void findByDescription() {
        UnitOfMeasure uom = new UnitOfMeasure();
        uom.setDescription(TEASPOON);

        UnitOfMeasure saved = repository.save(uom).block();

        UnitOfMeasure found = repository.findByDescription(TEASPOON).block();

        assertNotNull(found);
        assertEquals(TEASPOON, found.getDescription());
    }
}