package org.prashant.blog.blogapplicationapi.payload;

import jakarta.validation.constraints.NotBlank;
import org.prashant.blog.blogapplicationapi.entities.Tag;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record CreatePostRequest(String postTitle,
                                @NotBlank(message = "field cannot be empty")
                                String postContent,
                                String postDescription,
                                Long categoryId,
                                List<String> tags,
                                String bannerUrl,
                                Boolean draft
                                ) {
    public CreatePostRequest {
        System.out.println("CreatePostRequest created with postTitle: " + postTitle +
                ", postContent: " + postContent +
                ", postDescription: " + postDescription +
                ", categoryId: " + categoryId +
                ", tags: " + tags +
                ", bannerUrl: " + bannerUrl +
                ", draft: " + draft);
    }
}
