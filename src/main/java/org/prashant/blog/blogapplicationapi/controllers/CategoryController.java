package org.prashant.blog.blogapplicationapi.controllers;

import lombok.RequiredArgsConstructor;
import org.prashant.blog.blogapplicationapi.entities.Post;
import org.prashant.blog.blogapplicationapi.payload.ApiResponse;
import org.prashant.blog.blogapplicationapi.payload.CategoryDto;
import org.prashant.blog.blogapplicationapi.payload.CategoryPageResponse;
import org.prashant.blog.blogapplicationapi.payload.PostDto;
import org.prashant.blog.blogapplicationapi.service.CategoryService;
import org.prashant.blog.blogapplicationapi.utils.AppConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
public class CategoryController {
    private final CategoryService categoryService;
    @PostMapping("/")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CategoryDto> createCategory(@RequestBody CategoryDto categoryDto) {
        CategoryDto category = this.categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }
    @PutMapping("/")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CategoryDto> updateCategoryDiscAndTitle(@RequestBody CategoryDto categoryDto) {
        CategoryDto category = this.categoryService.updateCategoryDescriptionAndTitle(categoryDto);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }

    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long categoryId) {
        this.categoryService.deleteCategory(categoryId);
        return new ResponseEntity<>(new ApiResponse("category deleted successfully!!!", true), HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<CategoryPageResponse> getAllCategory(
            @RequestParam(value ="pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value ="pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.DEFAULT_CATEGORY_SORT_FIELD, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.DEFAULT_SORT_CRITERIA, required = false) String sortDir
    ){
        CategoryPageResponse categoryPageResponse = this.categoryService.getCategories(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(categoryPageResponse, HttpStatus.OK);
    }

}

