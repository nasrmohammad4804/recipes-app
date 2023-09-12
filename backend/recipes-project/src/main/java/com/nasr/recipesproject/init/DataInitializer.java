package com.nasr.recipesproject.init;

import com.nasr.recipesproject.dto.request.RecipesCategoryRequestDto;
import com.nasr.recipesproject.service.RecipesCategoryService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataInitializer {

    @Autowired
    private RecipesCategoryService recipesCategoryService;

    @PostConstruct
    public void initializeRecipesCategory() {

        long count = recipesCategoryService.getCount();

        if (count == 0) {
            createDefaultRecipesCategory();
        }
    }

    private void createDefaultRecipesCategory() {

        List<RecipesCategoryRequestDto> recipesCategoryRequestDtos = List.of(
            new RecipesCategoryRequestDto("Vegetables"),
            new RecipesCategoryRequestDto("Fruit"),
            new RecipesCategoryRequestDto("Seafood"),
            new RecipesCategoryRequestDto("Grain")
        );

        recipesCategoryService.saveAll(recipesCategoryRequestDtos);
    }
}
