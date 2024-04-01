package org.prashant.blog.blogapplicationapi.payload;

import jakarta.validation.constraints.NotBlank;

public record CreateTagRequest(
        @NotBlank(message = "empty tag is not allowed")
        String tagName
) {
}
