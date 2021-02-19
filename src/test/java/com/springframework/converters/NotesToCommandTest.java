package com.springframework.converters;

import com.springframework.commands.NotesCommand;
import com.springframework.domain.Notes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.*;

class NotesToCommandTest {
    public static final String ID = "1L";
    public static final String RECIPE_NOTES = "Notes";

    NotesToCommand converter;

    @BeforeEach
    void setUp() {
        converter = new NotesToCommand();
    }

    @Test
    void testNullParam() {
        assertNull(converter.convert(null));
    }

    @Test
    void testEmptyParam() {
        assertNotNull(converter.convert(new Notes()));
        assertThat(converter.convert(new Notes()),instanceOf(NotesCommand.class));
    }

    @Test
    void convert() {
        //given
        Notes notes = new Notes();
        notes.setId(ID);
        notes.setRecipeNotes(RECIPE_NOTES);

        //when
        NotesCommand command = converter.convert(notes);

        //then
        assertNotNull(command);
        assertEquals(ID, command.getId());
        assertEquals(RECIPE_NOTES,command.getRecipeNotes());
    }
}