package org.prashant.blog.blogapplicationapi.payload;

import org.prashant.blog.blogapplicationapi.entities.Post;

import java.util.Date;
import java.util.List;

public record PostDTO(
        Long postId,
        String title,
        String content,
        String description,
        String bannerUrl,
        Boolean draft,
        Date published,
        Date lastUpdated,
        List<TagDTO> tags,
        CategoryDTO category,
        UserDTO user,
        List<ActivityDTO> activities
) {
    public PostDTO(Post post) {
        this(post.getPostId(),
                post.getTitle(),
                post.getContent(),
                post.getDescription(),
                post.getBannerUrl(),
                post.getDraft(),
                post.getPublished(),
                post.getLastUpdated(),
                post.getTags().stream().map(TagDTO::new).toList(),
                new CategoryDTO(post.getCategory()),
                new UserDTO(post.getUser()),
                post.getPostActivities().stream().map(ActivityDTO::new).toList()
                );
        post.getPostActivities().forEach((System.out::println));
    }
}
