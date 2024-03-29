package org.prashant.blog.blogapplicationapi.payload;

import org.prashant.blog.blogapplicationapi.entities.Tag;

import java.util.Date;
import java.util.List;

public record PostUpdateRequest(
        Long postId,
        String postTitle,
        String postContent,
        Date lastUpdateDate,
        List<TagDto> tags
) {
}
