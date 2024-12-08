package org.prashant.blog.blogapplicationapi.controllers;

import lombok.RequiredArgsConstructor;
import org.prashant.blog.blogapplicationapi.entities.ActivityType;
import org.prashant.blog.blogapplicationapi.entities.User;
import org.prashant.blog.blogapplicationapi.payload.ActivityDTO;
import org.prashant.blog.blogapplicationapi.payload.PostPageResponse;
import org.prashant.blog.blogapplicationapi.service.ActivityService;
import org.prashant.blog.blogapplicationapi.service.UserService;
import org.prashant.blog.blogapplicationapi.utils.AppConstant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/activity")
public class UserActivityController {
    final private ActivityService activityService;
    final private UserService userService;
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
            @RequestParam(value ="pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value ="pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.DEFAULT_ACTIVITY_SORT_FIELD, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.DEFAULT_SORT_CRITERIA, required = false) String sortDir
    ) {
        User loggedInUser = userService.getLoggedInUser().orElseThrow(()-> new RuntimeException("Unauthorized User!!!"));
        return activityService.getPostsByActivityType(loggedInUser.getUserId(), ActivityType.LIKE, pageNumber, pageSize, sortBy, sortDir);
    }

    @GetMapping("/bookmarked")
    public PostPageResponse getBookmarkedPosts(
            @RequestParam(value ="pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value ="pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.DEFAULT_ACTIVITY_SORT_FIELD, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.DEFAULT_SORT_CRITERIA, required = false) String sortDir
    ) {
        User loggedInUser = userService.getLoggedInUser().orElseThrow(()-> new RuntimeException("Unauthorized User!!!"));
        return activityService.getPostsByActivityType(loggedInUser.getUserId(), ActivityType.BOOKMARK, pageNumber, pageSize, sortBy, sortDir);
    }

    @GetMapping("/count/{postId}/{activityType}")
    public ResponseEntity<Long> countActivities(@PathVariable Long postId, @PathVariable ActivityType activityType) {
        long count = activityService.countActivities(postId, activityType);
        return ResponseEntity.ok(count);
    }
}
