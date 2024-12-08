package org.prashant.blog.blogapplicationapi.repository;

import org.prashant.blog.blogapplicationapi.entities.Activity;
import org.prashant.blog.blogapplicationapi.entities.ActivityType;
import org.prashant.blog.blogapplicationapi.entities.Post;
import org.prashant.blog.blogapplicationapi.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    Page<Activity> findByUserAndActivityType(User user, ActivityType activityType, Pageable pageable);
    Optional<Activity> findByUserAndPostAndActivityType(User user, Post post, ActivityType activityType);
    @Query("SELECT COUNT(a) FROM Activity a WHERE a.post.postId = :postId AND a.activityType = :activityType")
    long countByPostIdAndActivityType(@Param("postId") Long postId, @Param("activityType") ActivityType activityType);
}
