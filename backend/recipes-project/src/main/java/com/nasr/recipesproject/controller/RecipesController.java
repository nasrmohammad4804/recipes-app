package com.nasr.recipesproject.controller;

import com.nasr.recipesproject.dto.request.RecipesRequestDto;
import com.nasr.recipesproject.dto.response.RecipesResponseDto;
import com.nasr.recipesproject.exception.ResponseDto;
import com.nasr.recipesproject.service.RecipesService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/recipes")
public class RecipesController {

    @Autowired
    private RecipesService recipesService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) throws Exception {
        RecipesResponseDto dto = recipesService.getById(id);
        return ResponseEntity.ok(
                new ResponseDto(true, "ok", dto)
        );
    }

    @PostMapping("/{categoryId}")
    public ResponseEntity<ResponseDto> saveRecipes(@PathVariable Long categoryId,
                                                   @RequestParam @NotBlank String title,
                                                   @RequestParam @NotBlank String summary,
                                                   @RequestParam @NotBlank String detail,
                                                   @RequestParam @NotNull MultipartFile file) throws Exception {

        RecipesRequestDto recipes = RecipesRequestDto.builder()
                .recipesCategoryId(categoryId)
                .title(title).summary(summary)
                .detail(detail).file(file)
                .build();

        RecipesResponseDto responseDto = recipesService.save(recipes);

        return ResponseEntity.ok(
                new ResponseDto(true, "ok", responseDto)
        );
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllByCategory(@RequestParam(required = false) Long recipesCategoryId) throws ExecutionException, InterruptedException {

        List<RecipesResponseDto> dtos = recipesService.getAllByCategoryId(recipesCategoryId);
        return ResponseEntity.ok(
                new ResponseDto(true, "ok", dtos)
        );
    }
}
