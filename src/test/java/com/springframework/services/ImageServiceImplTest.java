package com.springframework.services;

import com.springframework.domain.Recipe;
import com.springframework.repositories.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceImplTest {

    @Mock
    RecipeRepository recipeRepository;

    ImageService service;

    @BeforeEach
    void setUp() {
        service = new ImageServiceImpl(recipeRepository);
    }

    @Test
    void saveImage() throws IOException {
        //given
        String id = "1L";
        MockMultipartFile multipartFile = new MockMultipartFile("imagefile","test.txt","text/plain",
                "recipe project".getBytes());

        Recipe recipe = new Recipe();
        recipe.setId(id);
        when(recipeRepository.findById(anyString())).thenReturn(Optional.of(recipe));

        ArgumentCaptor<Recipe> argumentCaptor = ArgumentCaptor.forClass(Recipe.class);

        //when
        service.saveImage(id, multipartFile);

        //then
        verify(recipeRepository).save(argumentCaptor.capture());
        Recipe savedRecipe = argumentCaptor.getValue();
        assertEquals(multipartFile.getBytes().length,savedRecipe.getImage().length);


    }
}