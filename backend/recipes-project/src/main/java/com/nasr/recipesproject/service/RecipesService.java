package com.nasr.recipesproject.service;

import com.nasr.recipesproject.dto.request.RecipesRequestDto;
import com.nasr.recipesproject.dto.response.RecipesResponseDto;
import io.minio.errors.*;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface RecipesService {

    RecipesResponseDto getById(Long id) throws Exception;

    List<RecipesResponseDto> getAllByCategoryId(Long recipesCategoryId) throws ExecutionException, InterruptedException;

    RecipesResponseDto save(RecipesRequestDto recipesRequest) throws Exception;
}
