package com.springframework.controllers;

import com.springframework.commands.RecipeCommand;
import com.springframework.services.ImageService;
import com.springframework.services.RecipeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Controller
public class ImageController {
    private final ImageService imageService;
    private final RecipeService recipeService;

    public ImageController(ImageService imageService, RecipeService recipeService) {
        this.imageService = imageService;
        this.recipeService = recipeService;
    }

    @GetMapping("/recipe/{recipeId}/image")
    public String getImageForm(@PathVariable String recipeId, Model model){
        model.addAttribute("recipe", recipeService.findCommandById(recipeId).block());
        log.debug("found recipe id" + recipeId);
        return "recipe/imageuploadform";
    }

    @PostMapping("/recipe/{recipeId}/image")
    public String uploadImage(@PathVariable String recipeId, @RequestParam("imagefile") MultipartFile file) {
        imageService.saveImage(recipeId, file).block();
        return "redirect:/recipe/"+recipeId+"/show";
    }

    @GetMapping("recipe/{recipeId}/recipeimage")
    public void renderImageFromDB(@PathVariable String recipeId, HttpServletResponse response) throws IOException {
        RecipeCommand command = recipeService.findCommandById(recipeId).block();
        if (command.getImage() != null) {

            byte[] imgPrimitive = new byte[command.getImage().length];

            for (int i = 0; i < imgPrimitive.length; i++)
                imgPrimitive[i] = command.getImage()[i];

            response.setContentType("image/jpeg");
            InputStream is = new ByteArrayInputStream(imgPrimitive);
            IOUtils.copy(is, response.getOutputStream());
            log.debug("Image is rendered from DB for recipe id" + recipeId);
        } else {
            log.debug("image is not present");
        }
    }
}
