package com.springframework.services;

import com.springframework.commands.UnitOfMeasureCommand;
import com.springframework.converters.UnitOfMeasureToCommand;
import com.springframework.domain.UnitOfMeasure;
import com.springframework.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UnitOfMeasureServiceImplTest {

    UnitOfMeasureToCommand uomConverter = new UnitOfMeasureToCommand();
    UnitOfMeasureService service;

    @Mock
    UnitOfMeasureRepository uomRepo;

    @BeforeEach
    void setUp() {
        service = new UnitOfMeasureServiceImpl(uomRepo, uomConverter);
    }

    @Test
    void listAllUoms() {
        //given
        UnitOfMeasure uomc1 = new UnitOfMeasure();
        uomc1.setId("1L");
        UnitOfMeasure uomc2 = new UnitOfMeasure();
        uomc2.setId("2L");
        Set<UnitOfMeasure> uomcSet = new HashSet<>();
        Collections.addAll(uomcSet, uomc1, uomc2);

        when(uomRepo.findAll()).thenReturn(uomcSet);

        //when
        Set<UnitOfMeasureCommand> foundSet = service.listAllUoms();

        //then
        assertNotNull(foundSet);
        assertEquals(uomcSet.size(), foundSet.size());
        //verify(uomConverter).convert(any());
        verify(uomRepo).findAll();
    }
}