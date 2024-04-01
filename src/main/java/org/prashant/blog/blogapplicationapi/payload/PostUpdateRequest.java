package org.prashant.blog.blogapplicationapi.payload;

import org.prashant.blog.blogapplicationapi.entities.Tag;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

public record PostUpdateRequest(
        Long postId,
        String postTitle,
        String postContent,
        List<TagDto> tags,
        MultipartFile postImage,

        Long userId
) {
}
