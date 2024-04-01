package org.prashant.blog.blogapplicationapi.payload;

public record UpdateCommentRequest(
        Long commentId,
        String commentText,
        Long userId
) {
}
