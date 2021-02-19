package com.springframework.converters;

import com.springframework.commands.NotesCommand;
import com.springframework.domain.Notes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.*;

class CommandToNotesTest {
    public static final String ID = "1L";
    public static final String RECIPE_NOTES = "notes";

    CommandToNotes converter;

    @BeforeEach
    void setUp() {
        converter = new CommandToNotes();
    }

    @Test
    void testNullParam() {
        assertNull(converter.convert(null));
    }

    @Test
    void testEmptyParam() {
        assertNotNull(converter.convert(new NotesCommand()));
        assertThat(converter.convert(new NotesCommand()),instanceOf(Notes.class));
    }

    @Test
    void convert() {
        //given
        NotesCommand command = new NotesCommand();
        command.setId(ID);
        command.setRecipeNotes(RECIPE_NOTES);

        //when
        Notes notes = converter.convert(command);

        //then
        assertNotNull(notes);
        assertEquals(ID,notes.getId());
        assertEquals(RECIPE_NOTES,notes.getRecipeNotes());
    }
}