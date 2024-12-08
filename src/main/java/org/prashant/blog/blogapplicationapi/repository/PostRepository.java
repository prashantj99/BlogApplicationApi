package org.prashant.blog.blogapplicationapi.repository;

import org.prashant.blog.blogapplicationapi.entities.Category;
import org.prashant.blog.blogapplicationapi.entities.Post;
import org.prashant.blog.blogapplicationapi.entities.Tag;
import org.prashant.blog.blogapplicationapi.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByUser(User user, Pageable p);
    Page<Post> findByCategory(Category category, Pageable p);
    @Query("SELECT p FROM Post p WHERE LOWER(p.title) LIKE %:keyword% OR LOWER(p.content) LIKE %:keyword%")
    Page<Post> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String keyword, Pageable pageable);
    Page<Post> findByTagsContaining(Tag tag, Pageable pageable);
    Page<Post> findPostByUserAndDraft(User user, boolean draft, Pageable pageable);
    Page<Post> findByCategoryIn(List<Category> categories, Pageable pageable);
    @Query("SELECT p FROM Post p WHERE p.published >= :oneWeekAgo ORDER BY SIZE(p.postActivities) + SIZE(p.comments) DESC")
    Page<Post> findTrendingPosts(LocalDateTime oneWeekAgo, Pageable pageable);
    @Query("SELECT p FROM Post p WHERE p.category = :category ORDER BY (SIZE(p.comments) + SIZE(p.postActivities)) DESC")
    List<Post> findPopularPostsByCategory(Category category, PageRequest pageRequest);
    @Query("SELECT p.title FROM Post p WHERE p.title LIKE %:prefix% ORDER BY p.title ASC")
    Page<String> findBlogSuggestions(@Param("prefix") String prefix, Pageable pageable);

    Page<Post> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    @Query("SELECT p FROM Post p JOIN p.tags t WHERE t.name LIKE %:keyword%")
    Page<Post> findPostsByTagNameContaining(String keyword, Pageable pageable);
    @Modifying
    @Query("UPDATE Post p SET p.views = p.views + 1 WHERE p.id = :id")
    void incrementViews(@Param("id") Long id);
}
