package com.nasr.recipesproject.controller;

import com.nasr.recipesproject.dto.response.RecipesCategoryResponseDto;
import com.nasr.recipesproject.exception.ResponseDto;
import com.nasr.recipesproject.service.RecipesCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recipes-category")
public class RecipesCategoryController {

    @Autowired
    private RecipesCategoryService recipesCategoryService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllRecipesCategory(){

        List<RecipesCategoryResponseDto> recipesCategories =
                recipesCategoryService.getRecipesCategories();

        return ResponseEntity.ok(
                new ResponseDto(true,"ok",recipesCategories)
        );
    }
}
