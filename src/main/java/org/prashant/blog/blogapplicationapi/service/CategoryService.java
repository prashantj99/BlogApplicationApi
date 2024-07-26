package org.prashant.blog.blogapplicationapi.service;

import org.prashant.blog.blogapplicationapi.payload.CategoryDT;
import org.prashant.blog.blogapplicationapi.payload.CategoryDto;
import org.prashant.blog.blogapplicationapi.payload.CategoryPageResponse;

import java.util.List;

public interface CategoryService {
    CategoryDT createCategory(CategoryDT category);
    CategoryDT updateCategoryDescriptionAndTitle(CategoryDT category);
    void deleteCategory(Long categoryId);
    CategoryPageResponse getCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    List<CategoryDT> getAllCategories();

}
