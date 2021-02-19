package com.springframework.converters;

import com.springframework.commands.UnitOfMeasureCommand;
import com.springframework.domain.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.*;

class UnitOfMeasureToCommandTest {
    public static final String ID = "1L";
    public static final String DESCRIPTION = "description";

    UnitOfMeasureToCommand converter;

    @BeforeEach
    void setUp() {
        converter = new UnitOfMeasureToCommand();
    }

    @Test
    void testNullParam(){
        assertNull(converter.convert(null));
    }

    @Test
    void testEmptyParam() {
        assertNotNull(converter.convert(new UnitOfMeasure()));
        assertThat(converter.convert(new UnitOfMeasure()),instanceOf(UnitOfMeasureCommand.class));
    }

    @Test
    void convert() {
        //given
        UnitOfMeasure uom = new com.springframework.domain.UnitOfMeasure();
        uom.setId(ID);
        uom.setDescription(DESCRIPTION);

        //when
        UnitOfMeasureCommand command = converter.convert(uom);

        //then
        assertNotNull(command);
        assertEquals(ID, command.getId());
        assertEquals(DESCRIPTION,command.getDescription());
    }
}