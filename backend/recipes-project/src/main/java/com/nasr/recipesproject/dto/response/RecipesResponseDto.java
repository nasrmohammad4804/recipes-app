package com.nasr.recipesproject.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipesResponseDto {

    private Long id;
    private String title;
    private String summary;
    private String detail;

    private FileDto imageInfo;
}
