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
    @Query("SELECT a.post.postId, COUNT(a.activityId) FROM Activity a WHERE a.activityDate > :startDate AND a.activityType = :activityType GROUP BY a.post.postId")
    List<Object[]> countActivitiesByPostId(@Param("startDate") LocalDateTime startDate, @Param("activityType") ActivityType activityType);
}
