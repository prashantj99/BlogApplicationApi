package org.prashant.blog.blogapplicationapi.payload;

import org.prashant.blog.blogapplicationapi.entities.Post;

import java.util.Date;
import java.util.List;

public record PostDT(
        Long postId,
        String title,
        String content,
        String description,
        String bannerUrl,
        Boolean draft,
        Date published,
        Date lastUpdated,
        List<TagDT> tags,
        CategoryDT category,
        UserDT user,
        List<ActivityDT> activities
) {
    public PostDT(Post post) {
        this(post.getPostId(),
                post.getTitle(),
                post.getContent(),
                post.getDescription(),
                post.getBannerUrl(),
                post.getDraft(),
                post.getPublished(),
                post.getLastUpdated(),
                post.getTags().stream().map(TagDT::new).toList(),
                new CategoryDT(post.getCategory()),
                new UserDT(post.getUser()),
                post.getPostActivities().stream().map(ActivityDT::new).toList()
                );
        post.getPostActivities().forEach((System.out::println));
    }
}
