package com.nasr.recipesproject.domain;

import com.nasr.recipesproject.base.domain.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

import static com.nasr.recipesproject.domain.RecipesCategory.*;
import static jakarta.persistence.CascadeType.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = TABLE_NAME)
public class RecipesCategory  extends BaseEntity<Long> {

    public static final String TABLE_NAME="recipes_category";

    private String title;

    @OneToMany(mappedBy = "category",cascade = ALL)
    private List<Recipes> recipes = new ArrayList<>();

    public RecipesCategory(String title) {
        this.title = title;
    }
}
