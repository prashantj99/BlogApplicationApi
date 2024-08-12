package org.prashant.blog.blogapplicationapi.payload;

import org.prashant.blog.blogapplicationapi.entities.Category;
import org.prashant.blog.blogapplicationapi.entities.User;

import java.util.List;

public record CategoryDTO(
        Long categoryId,
        String title,
        String description,
        List<Long> subscribers
) {
    public CategoryDTO(Category category) {
        this(
                category.getCategoryId(),
                category.getTitle(),
                category.getDescription(),
                category.getSubscribers().stream().map(User::getUserId).toList()
        );
    }
}
