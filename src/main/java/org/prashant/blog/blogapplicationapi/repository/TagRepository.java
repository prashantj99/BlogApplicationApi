package org.prashant.blog.blogapplicationapi.repository;

import org.prashant.blog.blogapplicationapi.entities.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String tagName);
    @Query("SELECT t.name FROM Tag t WHERE t.name LIKE %:prefix% ORDER BY t.name ASC")
    Page<String> findTagSuggestions(String prefix, Pageable pageable);
}
