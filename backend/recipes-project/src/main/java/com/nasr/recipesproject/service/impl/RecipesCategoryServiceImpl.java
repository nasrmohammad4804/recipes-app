package com.nasr.recipesproject.service.impl;

import com.nasr.recipesproject.domain.RecipesCategory;
import com.nasr.recipesproject.dto.request.RecipesCategoryRequestDto;
import com.nasr.recipesproject.dto.response.RecipesCategoryResponseDto;
import com.nasr.recipesproject.exception.BusinessException;
import com.nasr.recipesproject.repository.RecipesCategoryRepository;
import com.nasr.recipesproject.service.RecipesCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Service
@Transactional(readOnly = true)
public class RecipesCategoryServiceImpl implements RecipesCategoryService {

    @Autowired
    private RecipesCategoryRepository repository;

    @Override
    @Transactional
    public void saveAll(List<RecipesCategoryRequestDto> recipesCategoryList) {

        List<RecipesCategory> recipesCategories = new ArrayList<>();

        recipesCategoryList.forEach(recipesCategory -> {

            recipesCategories.add(
                    new RecipesCategory(recipesCategory.getTitle())
            );
        });

        repository.saveAll(recipesCategories);

    }

    @Override
    public long getCount() {
        return repository.count();
    }

    @Override
    public List<RecipesCategoryResponseDto> getRecipesCategories() {
        return repository.findAllRecipesCategories();
    }

    @Override
    public RecipesCategory getById(Long id) {

        return repository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        "dont find any recipes category with this id"
                        ,
                        NOT_FOUND
                ));
    }


}
