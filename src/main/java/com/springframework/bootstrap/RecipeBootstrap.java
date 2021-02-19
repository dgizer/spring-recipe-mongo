package com.springframework.bootstrap;

import com.springframework.domain.*;
import com.springframework.enums.Difficulty;
import com.springframework.repositories.CategoryRepository;
import com.springframework.repositories.RecipeRepository;
import com.springframework.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@Profile("default")
public class RecipeBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final RecipeRepository recipeRepository;
    private final CategoryRepository categoryRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public RecipeBootstrap(RecipeRepository recipeRepository,
                           CategoryRepository categoryRepository,
                           UnitOfMeasureRepository unitOfMeasureRepository) {
        log.debug("Injecting dependencies (repositories)");
        this.recipeRepository = recipeRepository;
        this.categoryRepository = categoryRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            recipeRepository.saveAll(getRecipes());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        log.debug("Loading Bootstrap data...");
    }

    private List<Recipe> getRecipes() throws MalformedURLException {
        List<Recipe> recipes = new ArrayList<>(2);

        //get UnitOfMeasures
       /* Optional<UnitOfMeasure> eachUomOpt = unitOfMeasureRepository.findByDescription("Each");
        if(!eachUomOpt.isPresent()) {
            throw new RuntimeException("Expected UOM not found");
        }

        Optional<UnitOfMeasure> tableSpoonUomOpt = unitOfMeasureRepository.findByDescription("Tablespoon");
        if(!tableSpoonUomOpt.isPresent()) {
            throw new RuntimeException("Expected UOM not found");
        }

        Optional<UnitOfMeasure> teaSpoonUomOpt = unitOfMeasureRepository.findByDescription("Teaspoon");
        if(!teaSpoonUomOpt.isPresent()) {
            throw new RuntimeException("Expected UOM not found");
        }

        Optional<UnitOfMeasure> dashUomOpt = unitOfMeasureRepository.findByDescription("Dash");
        if(!dashUomOpt.isPresent()) {
            throw new RuntimeException("Expected UOM not found");
        }

        Optional<UnitOfMeasure> pintUomOpt = unitOfMeasureRepository.findByDescription("Pint");
        if(!pintUomOpt.isPresent()) {
            throw new RuntimeException("Expected UOM not found");
        }

        Optional<UnitOfMeasure> cupsUomOpt = unitOfMeasureRepository.findByDescription("Cup");
        if(!cupsUomOpt.isPresent()) {
            throw new RuntimeException("Expected UOM not found");
        }

        //get optionals
        UnitOfMeasure eachUom = eachUomOpt.get();
        UnitOfMeasure tableSpoonUom = tableSpoonUomOpt.get();
        UnitOfMeasure teaSpoonUom = teaSpoonUomOpt.get();
        UnitOfMeasure dashUom = dashUomOpt.get();
        UnitOfMeasure pintUom = pintUomOpt.get();
        UnitOfMeasure cupsUom = cupsUomOpt.get();*/

        //get optionals
        UnitOfMeasure eachUom = getfromOpt(unitOfMeasureRepository.findByDescription("Each"));
        UnitOfMeasure tableSpoonUom = getfromOpt(unitOfMeasureRepository.findByDescription("Tablespoon"));
        UnitOfMeasure teaSpoonUom = getfromOpt(unitOfMeasureRepository.findByDescription("Teaspoon"));
        UnitOfMeasure dashUom = getfromOpt(unitOfMeasureRepository.findByDescription("Dash"));
        UnitOfMeasure pintUom = getfromOpt(unitOfMeasureRepository.findByDescription("Pint"));
        UnitOfMeasure cupsUom = getfromOpt(unitOfMeasureRepository.findByDescription("Cup"));

        //get Catregories
        Optional<Category> americanCatOpt = categoryRepository.findByDescription("American");
        if (!americanCatOpt.isPresent()){
            throw new RuntimeException("Expected Category not found");
        }

        Optional<Category> mexicanCatOpt = categoryRepository.findByDescription("Mexican");
        if (!mexicanCatOpt.isPresent()){
            throw new RuntimeException("Expected Category not found");
        }

        Category americanCat = americanCatOpt.get();
        Category mexicanCat = mexicanCatOpt.get();

        //Yummy Guac
        Recipe guacRecipe = new Recipe();
        guacRecipe.setDescription("Perfect Guacamole");
        guacRecipe.setPrepTime(10);
        guacRecipe.setCookTime(0);
        guacRecipe.setDifficulty(Difficulty.EASY);
        guacRecipe.setServings(4);
        guacRecipe.setSource("Simply Recipes");
        guacRecipe.setUrl("https://www.simplyrecipes.com/recipes/perfect_guacamole/");
        guacRecipe.setDirections("1 Cut the avocado, remove flesh: Cut the avocados in half. Remove the pit. Score the inside of the avocado with a blunt knife and scoop out the flesh with a spoon. Place in a bowl.\n" +
                "2 Mash with a fork: Using a fork, roughly mash the avocado. (Don't overdo it! The guacamole should be a little chunky.)\n" +
                "3 Add salt, lime juice, and the rest: Sprinkle with salt and lime (or lemon) juice. The acid in the lime juice will provide some balance to the richness of the avocado and will help delay the avocados from turning brown.\n" +
                "\n" +
                "Add the chopped onion, cilantro, black pepper, and chiles. Chili peppers vary individually in their hotness. So, start with a half of one chili pepper and add to the guacamole to your desired degree of hotness.\n" +
                "\n" +
                "Remember that much of this is done to taste because of the variability in the fresh ingredients. Start with this recipe and adjust to your taste.\n" +
                "\n" +
                "Chilling tomatoes hurts their flavor, so if you want to add chopped tomato to your guacamole, add it just before serving.\n" +
                "4 Serve: Serve immediately, or if making a few hours ahead, place plastic wrap on the surface of the guacamole and press down to cover it and to prevent air reaching it. (The oxygen in the air causes oxidation which will turn the guacamole brown.) Refrigerate until ready to serve.");
        Notes guacNotes = new Notes();
        guacNotes.setRecipeNotes("Once you have basic guacamole down, feel free to experiment with variations including strawberries, peaches, pineapple, mangoes, even watermelon. One classic Mexican guacamole has pomegranate seeds and chunks of peaches in it (a Diana Kennedy favorite). You can get creative with homemade guacamole!\n" +
                "\n" +
                "Simple Guacamole: The simplest version of guacamole is just mashed avocados with salt. Don’t let the lack of availability of other ingredients stop you from making guacamole.\n" +
                "Quick guacamole: For a very quick guacamole just take a 1/4 cup of salsa and mix it in with your mashed avocados.\n" +
                "Don’t have enough avocados? To extend a limited supply of avocados, add either sour cream or cottage cheese to your guacamole dip. Purists may be horrified, but so what? It tastes great.");
        guacRecipe.setNotes(guacNotes);

        guacRecipe.addIngredient(new Ingredient("ripe avocados", new BigDecimal(2),eachUom));
        guacRecipe.addIngredient(new Ingredient("salt", new BigDecimal(0.25), teaSpoonUom));
        guacRecipe.addIngredient(new Ingredient("lemon juice", new BigDecimal(1), tableSpoonUom));
        guacRecipe.addIngredient(new Ingredient("red onion", new BigDecimal(2), tableSpoonUom));
        guacRecipe.addIngredient(new Ingredient("serrano chiles, stems and seeds removed, minced", new BigDecimal(2), eachUom));
        guacRecipe.addIngredient(new Ingredient("cilantro (leaves and tender stems), finely chopped", new BigDecimal(2), tableSpoonUom));
        guacRecipe.addIngredient(new Ingredient("freshly grated black pepper", new BigDecimal(1), dashUom));
        guacRecipe.addIngredient(new Ingredient("ripe tomato, seeds and pulp removed, chopped", new BigDecimal(0.5), eachUom));

        guacRecipe.getCategories().add(americanCat);
        guacRecipe.getCategories().add(mexicanCat);

//        guacRecipe.setImage(prepareImgFromUrl("https://www.simplyrecipes.com/wp-content/uploads/2018/07/Guacamole-LEAD-1-768x1075.jpg"));

        recipes.add(guacRecipe);
        log.debug("Guacamole recipe is ready...");

        //Yummy Tacos
        Recipe tacosRecipe = new Recipe();
        tacosRecipe.setDescription("Chicken Tacos");
        tacosRecipe.setPrepTime(30);
        tacosRecipe.setCookTime(0);
        tacosRecipe.setDifficulty(Difficulty.EASY);
        tacosRecipe.setServings(6);
        tacosRecipe.setSource("Simply Recipes");
        tacosRecipe.setUrl("https://www.simplyrecipes.com/recipes/spicy_grilled_chicken_tacos/");
        tacosRecipe.setDirections("1 Prepare a gas or charcoal grill for medium-high, direct heat.\n" +
                "\n" +
                "2 Make the marinade and coat the chicken: In a large bowl, stir together the chili powder, oregano, cumin, sugar, salt, garlic and orange zest. Stir in the orange juice and olive oil to make a loose paste. Add the chicken to the bowl and toss to coat all over.\n" +
                "\n" +
                "Set aside to marinate while the grill heats and you prepare the rest of the toppings.\n" +
                "3 Grill the chicken: Grill the chicken for 3 to 4 minutes per side, or until a thermometer inserted into the thickest part of the meat registers 165F. Transfer to a plate and rest for 5 minutes.\n" +
                "\n" +
                "4 Warm the tortillas: Place each tortilla on the grill or on a hot, dry skillet over medium-high heat. As soon as you see pockets of the air start to puff up in the tortilla, turn it with tongs and heat for a few seconds on the other side.\n" +
                "\n" +
                "Wrap warmed tortillas in a tea towel to keep them warm until serving.\n" +
                "\n" +
                "5 Assemble the tacos: Slice the chicken into strips. On each tortilla, place a small handful of arugula. Top with chicken slices, sliced avocado, radishes, tomatoes, and onion slices. Drizzle with the thinned sour cream. Serve with lime wedges.");
        Notes tacoNotes = new Notes();
        tacoNotes.setRecipeNotes("We have a family motto and it is this: Everything goes better in a tortilla.\n" +
                "\n" +
                "Any and every kind of leftover can go inside a warm tortilla, usually with a healthy dose of pickled jalapenos. I can always sniff out a late-night snacker when the aroma of tortillas heating in a hot pan on the stove comes wafting through the house.\n" +
                "Today’s tacos are more purposeful – a deliberate meal instead of a secretive midnight snack!\n" +
                "\n" +
                "\n" +
                "First, I marinate the chicken briefly in a spicy paste of ancho chile powder, oregano, cumin, and sweet orange juice while the grill is heating. You can also use this time to prepare the taco toppings.\n" +
                "\n" +
                "Grill the chicken, then let it rest while you warm the tortillas. Now you are ready to assemble the tacos and dig in. The whole meal comes together in about 30 minutes!\n" +
                "The ancho chiles I use in the marinade are named for their wide shape. They are large, have a deep reddish brown color when dried, and are mild in flavor with just a hint of heat. You can find ancho chile powder at any markets that sell Mexican ingredients, or online.\n" +
                "\n" +
                "I like to put all the toppings in little bowls on a big platter at the center of the table: avocados, radishes, tomatoes, red onions, wedges of lime, and a sour cream sauce. I add arugula, as well – this green isn’t traditional for tacos, but we always seem to have some in the fridge and I think it adds a nice green crunch to the tacos.\n" +
                "\n" +
                "Everyone can grab a warm tortilla from the pile and make their own tacos just they way they like them.\n" +
                "\n" +
                "You could also easily double or even triple this recipe for a larger party. A taco and a cold beer on a warm day? Now that’s living!");
        tacosRecipe.setNotes(tacoNotes);
        tacoNotes.setRecipe(tacosRecipe);

        tacosRecipe.addIngredient(new Ingredient("ancho chili powder", new BigDecimal(2), tableSpoonUom));
        tacosRecipe.addIngredient(new Ingredient("dried oregano", new BigDecimal(1), tableSpoonUom));
        tacosRecipe.addIngredient(new Ingredient("dried cumin", new BigDecimal(1), teaSpoonUom));
        tacosRecipe.addIngredient(new Ingredient("sugar", new BigDecimal(1), teaSpoonUom));
        tacosRecipe.addIngredient(new Ingredient("salt", new BigDecimal(0.5), teaSpoonUom));
        tacosRecipe.addIngredient(new Ingredient("clove garlic, finely chopped", new BigDecimal(1), eachUom));
        tacosRecipe.addIngredient(new Ingredient("finely grated orange zest", new BigDecimal(1), tableSpoonUom));
        tacosRecipe.addIngredient(new Ingredient("fresh-squeezed orange juice", new BigDecimal(3), tableSpoonUom));
        tacosRecipe.addIngredient(new Ingredient("olive oil", new BigDecimal(2), tableSpoonUom));
        tacosRecipe.addIngredient(new Ingredient("skinless, boneless chicken thighs", new BigDecimal(6), eachUom));

        tacosRecipe.addIngredient(new Ingredient("small corn tortillas", new BigDecimal(8), eachUom));
        tacosRecipe.addIngredient(new Ingredient("packed baby arugula", new BigDecimal(3), cupsUom));
        tacosRecipe.addIngredient(new Ingredient("medium ripe avocados, sliced", new BigDecimal(2), eachUom));
        tacosRecipe.addIngredient(new Ingredient("radishes, thinly sliced", new BigDecimal(4), eachUom));
        tacosRecipe.addIngredient(new Ingredient("cherry tomatoes, halved", new BigDecimal(0.5), pintUom));
        tacosRecipe.addIngredient(new Ingredient("red onion, thinly sliced", new BigDecimal(0.25), eachUom));
        tacosRecipe.addIngredient(new Ingredient("roughly chopped cilantro", new BigDecimal(1), eachUom));
        tacosRecipe.addIngredient(new Ingredient("sour cream thinned with", new BigDecimal(0.5), cupsUom));
        tacosRecipe.addIngredient(new Ingredient("milk", new BigDecimal(0.25), cupsUom));
        tacosRecipe.addIngredient(new Ingredient("lime, cut into wedges", new BigDecimal(1), eachUom));

        tacosRecipe.getCategories().add(americanCat);
        tacosRecipe.getCategories().add(mexicanCat);

        //tacosRecipe.setImage(prepareImgFromUrl("https://www.simplyrecipes.com/wp-content/uploads/2017/05/2017-05-29-GrilledChickenTacos-3-768x1075.jpg"));

        recipes.add(tacosRecipe);
        log.debug("Tacos receip is ready...");

        return recipes;
    }

    private Byte[] prepareImgFromUrl(String strurl) {
        Byte[] byteImg = null;
        URL url = null;
        try {
            url = new URL(strurl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        InputStream is = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            is = url.openStream();
            byte[] byteChunk = new byte[4096];
            int n;
            while ((n = is.read(byteChunk)) > 0 ){
                baos.write(byteChunk,0,n);
            }
            byteImg = new Byte[baos.toByteArray().length];
            for (int i=0; i<byteImg.length; i++)
                byteImg[i] = baos.toByteArray()[i];

        } catch (IOException e) {
            System.err.printf("Failed while reading bytes from %s: %s", url.toExternalForm(), e.getMessage());
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return byteImg;
    }

    private UnitOfMeasure getfromOpt(Optional<UnitOfMeasure> uomOpt){

        if (!uomOpt.isPresent()){
            throw new RuntimeException("Expected Category not found");
        } else {
            return uomOpt.get();
        }

    }

}
