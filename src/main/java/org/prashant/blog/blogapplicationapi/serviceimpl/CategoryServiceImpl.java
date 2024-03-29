package org.prashant.blog.blogapplicationapi.serviceimpl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.prashant.blog.blogapplicationapi.entities.Category;
import org.prashant.blog.blogapplicationapi.exceptions.ResourceNotFound;
import org.prashant.blog.blogapplicationapi.payload.CategoryDto;
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
    private final ModelMapper modelMapper;

    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = this.modelMapper.map(categoryDto, Category.class);
        Category saved_category = this.categoryRepository.save(category);
        return this.modelMapper.map(saved_category, CategoryDto.class);
    }

    @Override
    public CategoryDto updateCategoryDescriptionAndTitle(CategoryDto categoryDto) {
        Category category = this.categoryRepository.findById(categoryDto.getCategoryId())
                .orElseThrow(()-> new ResourceNotFound("Category", "categoryId", categoryDto.getCategoryId().toString()));
        //if category found then update
        category.setCategoryTitle(categoryDto.getCategoryTitle());
        category.setCategoryDescription(categoryDto.getCategoryDescription());
        Category updated_categoroy=this.categoryRepository.save(category);
        return this.modelMapper.map(updated_categoroy, CategoryDto.class);
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

        //create pageabel object
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

        //get requested users
        Page<Category> page_categories=this.categoryRepository.findAll(pageable);
        List<CategoryDto> categoryDtos = page_categories.getContent().stream().map(categoryDto -> this.modelMapper.map(categoryDto, CategoryDto.class)).toList();

        //return user response
        return new CategoryPageResponse(categoryDtos,
                page_categories.getNumber(), page_categories.getSize(),
                page_categories.getTotalElements(),
                page_categories.getTotalPages(), page_categories.isLast());
    }
}
