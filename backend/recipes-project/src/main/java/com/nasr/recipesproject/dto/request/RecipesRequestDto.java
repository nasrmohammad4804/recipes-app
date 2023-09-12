package com.nasr.recipesproject.dto.request;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Data
public class RecipesRequestDto {

    private Long recipesCategoryId;
    private String title;
    private String summary;
    private String detail;
    private MultipartFile file;
}
