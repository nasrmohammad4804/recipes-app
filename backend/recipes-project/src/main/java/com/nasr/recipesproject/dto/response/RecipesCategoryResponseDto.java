package com.nasr.recipesproject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class RecipesCategoryResponseDto {

    private Long id;
    private String title;

    public RecipesCategoryResponseDto(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
