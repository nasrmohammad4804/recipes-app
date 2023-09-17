package com.nasr.recipesproject.domain;

import com.nasr.recipesproject.base.domain.BaseEntity;
import com.nasr.recipesproject.dto.response.FileDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Recipes  extends BaseEntity<Long> {

    public static final String CATEGORY_ID="category_id";

    private String title;

    private String summary;

    @Column(columnDefinition = "text")
    private String detail;

    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = CATEGORY_ID)
    private RecipesCategory category;
}
