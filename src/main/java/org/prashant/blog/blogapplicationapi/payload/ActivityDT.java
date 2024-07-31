package org.prashant.blog.blogapplicationapi.payload;

import org.prashant.blog.blogapplicationapi.entities.Activity;
import org.prashant.blog.blogapplicationapi.entities.ActivityType;

public record ActivityDT(
        Long activityId,
        Long userId,
        ActivityType activityType
) {
    public ActivityDT(Activity acc){
        this(acc.getActivityId(), acc.getUser().getUserId(), acc.getActivityType());
    }
}
