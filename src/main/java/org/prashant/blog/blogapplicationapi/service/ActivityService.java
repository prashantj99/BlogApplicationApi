package org.prashant.blog.blogapplicationapi.service;

import org.prashant.blog.blogapplicationapi.entities.ActivityType;
import org.prashant.blog.blogapplicationapi.payload.ActivityDTO;
import org.prashant.blog.blogapplicationapi.payload.PostPageResponse;

public interface ActivityService {
    ActivityDTO performActivityOnPost(Long userId, Long postId, ActivityType activityType);
    PostPageResponse getPostsByActivityType(Long userId, ActivityType activityType, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    long countActivities(Long postId, ActivityType activityType);
}
