package org.prashant.blog.blogapplicationapi.service;

import org.prashant.blog.blogapplicationapi.entities.ActivityType;
import org.prashant.blog.blogapplicationapi.payload.*;

import java.io.IOException;
import java.util.List;

public interface PostService {
    PostDTO createPost(CreatePostRequest createPostRequest, Long userId) throws IOException;
    PostDTO updatePost(PostUpdateRequest postUpdateRequest) throws IOException;
    void deletePost(Long postId, Long userId) throws IOException;
    PostPageResponse getPosts(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    PostDTO getPostById(Long postId);
    PostPageResponse getPostsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    PostPageResponse getPostsByUser(Long userId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    PostPageResponse getPostsByTag(String tagName, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    PostPageResponse searchPosts(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    PostPageResponse getPublishedPostsByUser(Long userId, boolean draft, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    PostPageResponse getTrendingPosts(Integer pageNumber, Integer pageSize);
    List<PostDTO> getRecommendedPostsOfCategory(Long categoryId, Integer pageNumber, Integer pageSize);
}
