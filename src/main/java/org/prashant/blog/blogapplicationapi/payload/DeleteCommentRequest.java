package org.prashant.blog.blogapplicationapi.payload;

import jakarta.validation.constraints.NotBlank;

public record DeleteCommentRequest(
        @NotBlank(message = "comment id is must") Long commentId,
        @NotBlank(message = "user id is must") Long userId
        ) {
}
