package com.nasr.recipesproject.repository;

import com.nasr.recipesproject.domain.Recipes;
import com.nasr.recipesproject.dto.response.RecipesResponseDto;
import com.nasr.recipesproject.dto.response.RecipesSummaryResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecipesRepository extends JpaRepository<Recipes,Long> {

    @Query("select new com.nasr.recipesproject.dto.response.RecipesSummaryResponseDto(r.id,r.title,r.summary,r.image) " +
            "from Recipes as r join r.category as c " +
            "where (:recipesCategoryId is null  or c.id=:recipesCategoryId)")
    List<RecipesSummaryResponseDto> findByRecipesCategoryId(Long recipesCategoryId);
}
