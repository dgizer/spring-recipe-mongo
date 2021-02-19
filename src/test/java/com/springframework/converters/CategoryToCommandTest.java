package com.springframework.converters;

import com.springframework.commands.CategoryCommand;
import com.springframework.domain.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.*;

class CategoryToCommandTest {
    public static final String ID = "1L";
    public static final String DESCRIPTION = "Description";

    CategoryToCommand converter;

    @BeforeEach
    void setUp() {
        converter = new CategoryToCommand();
    }

    @Test
    void testNullParam() {
        assertNull(converter.convert(null));
    }

    @Test
    void testEmptyParam() {
        assertNotNull(converter.convert(new Category()));
        assertThat(converter.convert(new Category()), instanceOf(CategoryCommand.class));
    }

    @Test
    void convert() {
        //given
        Category category = new Category();
        category.setId(ID);
        category.setDescription(DESCRIPTION);

        //when
        CategoryCommand command = converter.convert(category);

        //then
        assertNotNull(command);
        assertEquals(ID, command.getId());
        assertEquals(DESCRIPTION, command.getDescription());
    }
}