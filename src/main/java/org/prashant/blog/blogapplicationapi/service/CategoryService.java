package org.prashant.blog.blogapplicationapi.service;

import org.prashant.blog.blogapplicationapi.payload.CategoryDto;
import org.prashant.blog.blogapplicationapi.payload.CategoryPageResponse;

public interface CategoryService {
    CategoryDto createCategory(CategoryDto categoryDto);
    CategoryDto updateCategoryDescriptionAndTitle(CategoryDto categoryDto);
    void deleteCategory(Long categoryId);
    CategoryPageResponse getCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

}
