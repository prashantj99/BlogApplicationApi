package org.prashant.blog.blogapplicationapi.payload;

import jakarta.validation.constraints.NotBlank;

public record DeletePostRequest(
        @NotBlank(message = "field cannot be empty") Long postId,
        @NotBlank(message = "field cannot be empty") Long userId
) {
}
