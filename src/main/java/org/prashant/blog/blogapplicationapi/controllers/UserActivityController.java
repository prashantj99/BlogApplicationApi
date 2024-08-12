package org.prashant.blog.blogapplicationapi.controllers;

import lombok.RequiredArgsConstructor;
import org.prashant.blog.blogapplicationapi.entities.ActivityType;
import org.prashant.blog.blogapplicationapi.payload.ActivityDTO;
import org.prashant.blog.blogapplicationapi.payload.PostPageResponse;
import org.prashant.blog.blogapplicationapi.service.ActivityService;
import org.prashant.blog.blogapplicationapi.utils.AppConstant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/activity")
public class UserActivityController {
    final private ActivityService activityService;
    @PostMapping("/like/post/{postId}")
    public ResponseEntity<?> likePost(@PathVariable Long postId, @RequestParam Long userId) {
        ActivityDTO activity = activityService.performActivityOnPost(userId, postId, ActivityType.LIKE);
        return ResponseEntity.ok(activity);
    }

    @PostMapping("/bookmark/post/{postId}")
    public ResponseEntity<?> bookmarkPost(@PathVariable Long postId, @RequestParam Long userId) {
        ActivityDTO activity = activityService.performActivityOnPost(userId, postId, ActivityType.BOOKMARK);
        return ResponseEntity.ok(activity);
    }

    @GetMapping("/liked")
    public PostPageResponse getLikedPosts(
            @RequestParam Long userId,
            @RequestParam(value ="pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value ="pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.DEFAULT_ACTIVITY_SORT_FIELD, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.DEFAULT_SORT_CRITERIA, required = false) String sortDir
    ) {
        return activityService.getPostsByActivityType(userId, ActivityType.LIKE, pageNumber, pageSize, sortBy, sortDir);
    }

    @GetMapping("/bookmarked")
    public PostPageResponse getBookmarkedPosts(
            @RequestParam Long userId,
            @RequestParam(value ="pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value ="pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.DEFAULT_ACTIVITY_SORT_FIELD, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.DEFAULT_SORT_CRITERIA, required = false) String sortDir
    ) {
        return activityService.getPostsByActivityType(userId, ActivityType.BOOKMARK, pageNumber, pageSize, sortBy, sortDir);
    }
}
