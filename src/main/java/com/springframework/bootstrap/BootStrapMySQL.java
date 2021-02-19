package com.springframework.bootstrap;

import com.springframework.domain.Category;
import com.springframework.domain.UnitOfMeasure;
import com.springframework.repositories.CategoryRepository;
import com.springframework.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile({"dev","prod"})
public class BootStrapMySQL implements ApplicationListener<ContextRefreshedEvent> {
    private final CategoryRepository categoryRepository;
    private final UnitOfMeasureRepository unitOfMeasureRepository;

    public BootStrapMySQL(CategoryRepository categoryRepository, UnitOfMeasureRepository unitOfMeasureRepository) {
        this.categoryRepository = categoryRepository;
        this.unitOfMeasureRepository = unitOfMeasureRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (categoryRepository.count() == 0L ) {
            log.debug("Loading Categories");
            loadCategories();
        }

        if (unitOfMeasureRepository.count() == 0L) {
            log.debug("Loading uoms");
            loadUoms();
        }
    }

    private void loadCategories(){
        Category cat1 = Category.builder().description("American").build();
        categoryRepository.save(cat1);

        Category cat2 = Category.builder().description("Italian").build();
        categoryRepository.save(cat2);

        Category cat3 = Category.builder().description("Mexican").build();
        categoryRepository.save(cat3);

        Category cat4 = Category.builder().description("Fast Food").build();
        categoryRepository.save(cat4);
    }

    private void loadUoms() {
        UnitOfMeasure uom1 = UnitOfMeasure.builder().description("Teaspoon").build();
        unitOfMeasureRepository.save(uom1);

        UnitOfMeasure uom2 = UnitOfMeasure.builder().description("Tablespoon").build();
        unitOfMeasureRepository.save(uom2);

        UnitOfMeasure uom3 = UnitOfMeasure.builder().description("Cup").build();
        unitOfMeasureRepository.save(uom3);

        UnitOfMeasure uom4 = UnitOfMeasure.builder().description("Pinch").build();
        unitOfMeasureRepository.save(uom4);

        UnitOfMeasure uom5 = UnitOfMeasure.builder().description("Ounce").build();
        unitOfMeasureRepository.save(uom5);

        UnitOfMeasure uom6 = UnitOfMeasure.builder().description("Dash").build();
        unitOfMeasureRepository.save(uom6);

        UnitOfMeasure uom7 = UnitOfMeasure.builder().description("Each").build();
        unitOfMeasureRepository.save(uom7);

        UnitOfMeasure uom8 = UnitOfMeasure.builder().description("Pint").build();
        unitOfMeasureRepository.save(uom8);
    }

}
