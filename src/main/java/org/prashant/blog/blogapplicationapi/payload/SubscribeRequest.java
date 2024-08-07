package org.prashant.blog.blogapplicationapi.payload;

public record SubscribeRequest(
        Long userId,
        Long categoryId
) {
}
