package org.prashant.blog.blogapplicationapi.payload;

import org.prashant.blog.blogapplicationapi.entities.Activity;
import org.prashant.blog.blogapplicationapi.entities.ActivityType;

public record ActivityDTO(
        Long activityId,
        Long userId,
        ActivityType activityType
) {
    public ActivityDTO(Activity acc){
        this(acc.getActivityId(), acc.getUser().getUserId(), acc.getActivityType());
    }
}
