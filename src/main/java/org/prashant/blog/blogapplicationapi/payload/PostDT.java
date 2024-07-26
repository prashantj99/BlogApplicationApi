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
        Long categoryId,
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
                post.getCategory().getCategoryId(),
                new UserDT(post.getUser())
                );
    }
}
