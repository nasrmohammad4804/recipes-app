package com.nasr.recipesproject.repository;

import com.nasr.recipesproject.domain.RecipesCategory;
import com.nasr.recipesproject.dto.response.RecipesCategoryResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecipesCategoryRepository extends JpaRepository<RecipesCategory,Long> {

    @Query("select  new com.nasr.recipesproject.dto.response.RecipesCategoryResponseDto(ce.id,ce.title) from RecipesCategory  as ce")
    List<RecipesCategoryResponseDto> findAllRecipesCategories();
}
