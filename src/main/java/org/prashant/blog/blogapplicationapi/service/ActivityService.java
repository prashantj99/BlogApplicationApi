package org.prashant.blog.blogapplicationapi.service;

import org.prashant.blog.blogapplicationapi.entities.ActivityType;
import org.prashant.blog.blogapplicationapi.entities.Post;
import org.prashant.blog.blogapplicationapi.entities.User;
import org.prashant.blog.blogapplicationapi.payload.ActivityDT;
import org.prashant.blog.blogapplicationapi.payload.PostDT;
import org.prashant.blog.blogapplicationapi.payload.PostPageResponse;

import java.util.List;

public interface ActivityService {
    ActivityDT performActivityOnPost(Long userId, Long postId, ActivityType activityType);
    PostPageResponse getPostsByActivityType(Long userId, ActivityType activityType, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
}
