package com.springframework.converters;

import com.springframework.commands.UnitOfMeasureCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.*;

class CommandToUnitOfMeasureTest {
    public final static String DESCRIPTION = "description";
    public final static String ID = "1L";

    CommandToUnitOfMeasure converter;

    @BeforeEach
    void setUp() {
        converter = new CommandToUnitOfMeasure();
    }

    @Test
    void testNullParam() {
        assertNull(converter.convert(null));
    }

    @Test
    void testEmptyObj() {
        assertNotNull(converter.convert(new UnitOfMeasureCommand()));
        assertThat(converter.convert(new UnitOfMeasureCommand()),instanceOf(com.springframework.domain.UnitOfMeasure.class));
    }

    @Test
    void convert() {
        //given
        UnitOfMeasureCommand command = new UnitOfMeasureCommand();
        command.setId(ID);
        command.setDescription(DESCRIPTION);

        //when
        com.springframework.domain.UnitOfMeasure uom = converter.convert(command);

        //then
        assertNotNull(uom);
        assertEquals(ID, uom.getId());
        assertEquals(DESCRIPTION, uom.getDescription());
    }
}