package com.nasr.recipesproject.service;

import com.nasr.recipesproject.domain.RecipesCategory;
import com.nasr.recipesproject.dto.request.RecipesCategoryRequestDto;
import com.nasr.recipesproject.dto.response.RecipesCategoryResponseDto;

import java.util.List;

public interface RecipesCategoryService {

    void saveAll(List<RecipesCategoryRequestDto> recipesCategoryList);

    long getCount();

    List<RecipesCategoryResponseDto> getRecipesCategories();

    RecipesCategory getById(Long id);
}
