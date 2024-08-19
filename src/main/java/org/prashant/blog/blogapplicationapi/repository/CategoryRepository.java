package org.prashant.blog.blogapplicationapi.repository;

import org.prashant.blog.blogapplicationapi.entities.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c.title FROM Category c WHERE c.title LIKE %:prefix% ORDER BY c.title ASC")
    Page<String> findCategorySuggestions(String prefix, Pageable pageable);
    Page<Category> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
