package com.nasr.recipesproject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor

public class RecipesSummaryResponseDto {

    private Long id;
    private String title;
    private String summary;
    private String image;

    public RecipesSummaryResponseDto(Long id, String title, String summary, String image) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.image = image;
    }
}
