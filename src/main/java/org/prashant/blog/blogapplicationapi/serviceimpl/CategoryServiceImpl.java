package org.prashant.blog.blogapplicationapi.serviceimpl;

import lombok.RequiredArgsConstructor;
import org.prashant.blog.blogapplicationapi.entities.Category;
import org.prashant.blog.blogapplicationapi.exceptions.ResourceNotFound;
import org.prashant.blog.blogapplicationapi.payload.CategoryDTO;
import org.prashant.blog.blogapplicationapi.payload.CategoryPageResponse;
import org.prashant.blog.blogapplicationapi.repository.CategoryRepository;
import org.prashant.blog.blogapplicationapi.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDT) {
        Category category = new Category();
        category.setDescription(categoryDT.description());
        category.setTitle(categoryDT.title());
        Category saved_category = this.categoryRepository.save(category);
        return new CategoryDTO(saved_category);
    }

    @Override
    public CategoryDTO updateCategoryDescriptionAndTitle(CategoryDTO categoryDto) {
        Category category = this.categoryRepository.findById(categoryDto.categoryId())
                .orElseThrow(()-> new ResourceNotFound("Category", "categoryId", categoryDto.categoryId().toString()));

        //if category found then update
        category.setTitle(categoryDto.title());
        category.setDescription(categoryDto.description());
        Category updated_category=this.categoryRepository.save(category);
        return new CategoryDTO(updated_category);
    }

    @Override
    public void deleteCategory(Long categoryId) {
        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFound("Category", "categoryId", categoryId.toString()));
        this.categoryRepository.delete(category);
    }

    @Override
    public CategoryPageResponse getCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = Sort.by(sortBy).ascending();
        if(sortDir.equalsIgnoreCase("desc")){
            sort = Sort.by(sortBy).descending();
        }
        //create pageable object
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        //get requested users
        Page<Category> page_categories=this.categoryRepository.findAll(pageable);
        List<CategoryDTO> categories = page_categories.getContent().stream().map(CategoryDTO::new).toList();

        //return user response
        return new CategoryPageResponse(categories,
                page_categories.getNumber(),
                page_categories.getSize(),
                page_categories.getTotalElements(),
                page_categories.getTotalPages(),
                page_categories.isLast());
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
            return this.categoryRepository.findAll().stream().map(CategoryDTO::new).toList();
    }

    @Override
    public CategoryDTO getCategory(Long categoryId) {
        var category = this.categoryRepository.findById(categoryId).orElseThrow(() -> new ResourceNotFound("Category", "categoryId", categoryId.toString()));
        return new CategoryDTO(category);
    }
}
