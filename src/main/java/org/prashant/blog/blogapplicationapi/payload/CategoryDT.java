package org.prashant.blog.blogapplicationapi.payload;

import org.prashant.blog.blogapplicationapi.entities.Category;
import org.prashant.blog.blogapplicationapi.entities.User;

import java.util.Date;

public record CategoryDT(
        Long categoryId,
        String title,
        String description
) {
    public CategoryDT(Category category){
        this(category.getCategoryId(), category.getTitle(), category.getDescription());
    }
}
