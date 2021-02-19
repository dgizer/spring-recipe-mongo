package com.springframework.services;

import com.springframework.domain.Recipe;
import com.springframework.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
public class ImageServiceImpl implements ImageService {

    RecipeRepository recipeRepository;

    public ImageServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    @Transactional
    public void saveImage(String id, MultipartFile file) {
        log.debug("Save received image in recipe id:" + id);
        Optional<Recipe> recipeOpt = recipeRepository.findById(id);

        if(recipeOpt.isPresent()){
            try {
                Byte[] byteObj = new Byte[file.getBytes().length];
                for (int i=0;  i<file.getBytes().length; i++) {
                    byteObj[i] = file.getBytes()[i];
                }
                recipeOpt.get().setImage(byteObj);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            recipeRepository.save(recipeOpt.get());
        } else {
            //todo handle an exception
            log.debug("Recipe is not present: id "+id);
        }
    }
}
