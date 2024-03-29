package org.prashant.blog.blogapplicationapi.payload;

import jakarta.validation.constraints.NotBlank;
import org.prashant.blog.blogapplicationapi.entities.Tag;

import java.util.List;

public record CreatePostRequest(String postTitle,
                                @NotBlank(message = "field cannot be empty")
                                String postContent,
                                String imageName,
                                Long userId,
                                Long categoryId,
                                List<TagDto> tags

                                ) {
}
