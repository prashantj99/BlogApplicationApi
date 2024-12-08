package org.prashant.blog.blogapplicationapi.controllers;

import lombok.RequiredArgsConstructor;
import org.prashant.blog.blogapplicationapi.entities.Post;
import org.prashant.blog.blogapplicationapi.payload.*;
import org.prashant.blog.blogapplicationapi.service.CategoryService;
import org.prashant.blog.blogapplicationapi.utils.AppConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping("/")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody CategoryDTO categoryDto) {
        CategoryDTO new_category = this.categoryService.createCategory(categoryDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new_category);
    }

    @PutMapping("/")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CategoryDTO> updateCategoryDiscAndTitle(@RequestBody CategoryDTO categoryDto) {
        CategoryDTO updated_category = this.categoryService.updateCategoryDescriptionAndTitle(categoryDto);
        return ResponseEntity.ok(updated_category);
    }

    @DeleteMapping("/{categoryId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> deleteCategory(@PathVariable Long categoryId) {
        this.categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok(new ApiResponse("category deleted successfully!!!", true));
    }

    @PostMapping("/follow/{categoryId}")
    public ResponseEntity<?> followCategoryHandler(@PathVariable Long categoryId ){
        this.categoryService.followCategory(categoryId);
        return ResponseEntity.ok("success");
    }

    @DeleteMapping("/follow/{categoryId}")
    public ResponseEntity<?> unfollowCategoryHandler(@PathVariable Long categoryId ){
        this.categoryService.unfollowCategory(categoryId);
        return ResponseEntity.ok("success");
    }

    @GetMapping("/page")
    public ResponseEntity<CategoryPageResponse> getCategoryByPage(
            @RequestParam(value ="pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value ="pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.DEFAULT_CATEGORY_SORT_FIELD, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.DEFAULT_SORT_CRITERIA, required = false) String sortDir
    ){
        CategoryPageResponse categoryPageResponse = this.categoryService.getCategories(pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(categoryPageResponse);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CategoryDTO>> getAllCategory(){
        return ResponseEntity.ok(this.categoryService.getAllCategories());
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<?> getCategoryHandler(@PathVariable Long categoryId){
        return ResponseEntity.ok(this.categoryService.getCategory(categoryId));
    }

}

