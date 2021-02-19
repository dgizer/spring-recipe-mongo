package com.springframework.converters;

import com.springframework.commands.CategoryCommand;
import com.springframework.domain.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.*;

class CommandToCategoryTest {
    public static final String ID = "1L";
    public static final String DESCRIPTION = "Description";

    CommandToCategory converter;

    @BeforeEach
    void setUp() {
        converter = new CommandToCategory();
    }

    @Test
    void testNullParam() {
        assertNull(converter.convert(null));
    }

    @Test
    void testEmptyParam() {
        assertNotNull(converter.convert(new CategoryCommand()));
        assertThat(converter.convert(new CategoryCommand()), instanceOf(Category.class));
    }

    @Test
    void convert() {
        //given
        CategoryCommand command = new CategoryCommand();
        command.setId(ID);
        command.setDescription(DESCRIPTION);

        //when
        Category category = converter.convert(command);

        //then
        assertNotNull(category);
        assertEquals(ID, category.getId());
        assertEquals(DESCRIPTION,category.getDescription());
    }
}