package com.nasr.recipesproject.service.impl;

import com.nasr.recipesproject.domain.Recipes;
import com.nasr.recipesproject.domain.RecipesCategory;
import com.nasr.recipesproject.dto.request.RecipesRequestDto;
import com.nasr.recipesproject.dto.response.FileDto;
import com.nasr.recipesproject.dto.response.RecipesResponseDto;
import com.nasr.recipesproject.dto.response.RecipesSummaryResponseDto;
import com.nasr.recipesproject.exception.BusinessException;
import com.nasr.recipesproject.repository.RecipesRepository;
import com.nasr.recipesproject.service.MinioService;
import com.nasr.recipesproject.service.RecipesCategoryService;
import com.nasr.recipesproject.service.RecipesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@Transactional(readOnly = true)
public class RecipesServiceImpl implements RecipesService {

    @Autowired
    private RecipesRepository repository;

    @Autowired
    private RecipesCategoryService recipesCategoryService;

    @Autowired
    private MinioService minioService;

    @Override
    public RecipesResponseDto getById(Long id) throws Exception {

        Recipes recipes = repository.findById(id)
                .orElseThrow(() -> new BusinessException(
                        "dont find any recipes with this id"
                        ,
                        HttpStatus.NOT_FOUND
                ));

        FileDto object = minioService.getObjectByName(recipes.getImage());


        return getRecipesResponse(recipes,object);

    }

    @Override
    public List<RecipesResponseDto> getAllByCategoryId(Long recipesCategoryId) throws ExecutionException, InterruptedException {

        List<RecipesSummaryResponseDto> recipesList = repository.findByRecipesCategoryId(recipesCategoryId);

        List<CompletableFuture<?>> completableFutures = new ArrayList<>();

        List<RecipesResponseDto> recipesResponseDtos = new ArrayList<>();

        for (RecipesSummaryResponseDto summary : recipesList)
            completableFutures.add(
                    getCompletableFuture(recipesResponseDtos, summary)
            );

        CompletableFuture<Void> result = CompletableFuture
                .allOf(completableFutures.toArray(CompletableFuture[]::new));

        result.get();

        return  recipesResponseDtos;
    }

    @Override
    @Transactional
    public RecipesResponseDto save(RecipesRequestDto recipesRequest) throws Exception {

        String objectName = UUID.randomUUID().toString();

        RecipesCategory category = recipesCategoryService.getById(recipesRequest.getRecipesCategoryId());

        Recipes recipes = new Recipes();

        recipes.setTitle(recipesRequest.getTitle());
        recipes.setSummary(recipesRequest.getSummary());
        recipes.setDetail(recipesRequest.getDetail());
        recipes.setImage(objectName);
        recipes.setCategory(category);

        Recipes recipesEntity = repository.save(recipes);

        FileDto fileDto = minioService.uploadObject(recipesRequest.getFile(), objectName);

        return getRecipesResponse(recipesEntity,fileDto);
    }

    private CompletableFuture<Void> getCompletableFuture(List<RecipesResponseDto> recipesResponseDtos, RecipesSummaryResponseDto recipes) {

        return CompletableFuture.supplyAsync(() -> {

                    try {
                        return minioService.getObjectByName(recipes.getImage());
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })

                .thenAccept(object -> {

                    recipesResponseDtos.add(
                            RecipesResponseDto.builder()
                                    .id(recipes.getId())
                                    .title(recipes.getTitle())
                                    .summary(recipes.getSummary())
                                    .imageInfo(object)
                                    .build()
                    );
                });
    }

    private RecipesResponseDto getRecipesResponse(Recipes recipes, FileDto fileDto){

        return RecipesResponseDto.builder()
                .id(recipes.getId())
                .title(recipes.getTitle())
                .summary(recipes.getSummary())
                .detail(recipes.getDetail())
                .imageInfo(fileDto)
                .build();
    }


}
