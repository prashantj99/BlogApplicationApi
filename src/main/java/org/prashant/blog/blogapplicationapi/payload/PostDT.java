package org.prashant.blog.blogapplicationapi.payload;

import org.prashant.blog.blogapplicationapi.entities.Post;
import org.prashant.blog.blogapplicationapi.entities.Tag;

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
        UserDT userDT
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
                new UserDT(post.getUser())
                );
    }
}
