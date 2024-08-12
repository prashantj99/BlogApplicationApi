package org.prashant.blog.blogapplicationapi.payload;

public record CreateCommentRequest(
        String commentText,
        Long postId
) {
}
