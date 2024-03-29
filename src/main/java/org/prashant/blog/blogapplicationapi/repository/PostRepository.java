package org.prashant.blog.blogapplicationapi.repository;

import org.prashant.blog.blogapplicationapi.entities.Category;
import org.prashant.blog.blogapplicationapi.entities.Post;
import org.prashant.blog.blogapplicationapi.entities.Tag;
import org.prashant.blog.blogapplicationapi.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByUser(User user, Pageable p);
    Page<Post> findByCategory(Category category, Pageable p);
    @Query("SELECT p FROM Post p WHERE LOWER(p.postTitle) LIKE %:keyword% OR LOWER(p.postContent) LIKE %:keyword%")
    Page<Post> findByPostTitleContainingIgnoreCaseOrPostContentContainingIgnoreCase(String keyword, Pageable pageable);
    Page<Post> findByTagsContaining(Tag tag, Pageable pageable);
}
