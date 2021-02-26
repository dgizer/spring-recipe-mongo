package com.springframework.services;

import com.springframework.domain.Recipe;
import com.springframework.repositories.reactive.RecipeReactiveRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    RecipeReactiveRepository recipeRepository;

    public ImageServiceImpl(RecipeReactiveRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    public Mono<Void> saveImage(String id, MultipartFile file) {
        log.debug("Save received image in recipe id:" + id);

        Mono<Recipe> recipeMono = recipeRepository.findById(id)
                .map(recipe -> {
                    try {
                        Byte[] byteObj = new Byte[file.getBytes().length];
                        for (int i=0;  i<file.getBytes().length; i++) {
                            byteObj[i] = file.getBytes()[i];
                        }
                        recipe.setImage(byteObj);
                        return recipe;
                    } catch (IOException ioe) {
                        ioe.printStackTrace();
                        throw new RuntimeException(ioe);
                    }
                });
        recipeRepository.save(recipeMono.block()).block();
        return Mono.empty();
    }
}
