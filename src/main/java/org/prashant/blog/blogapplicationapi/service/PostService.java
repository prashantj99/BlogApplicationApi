package org.prashant.blog.blogapplicationapi.service;

import org.prashant.blog.blogapplicationapi.payload.*;

import java.io.IOException;

public interface PostService {
    PostDT createPost(CreatePostRequest createPostRequest, Long userId) throws IOException;
    PostDT updatePost(PostUpdateRequest postUpdateRequest) throws IOException;
    void deletePost(DeletePostRequest deletePostRequest) throws IOException;
    PostPageResponse getPosts(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    PostDT getPostById(Long postId);
    PostPageResponse getPostsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    PostPageResponse getPostsByUser(Long userId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    PostPageResponse getPostsByTag(String tagName, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    PostPageResponse searchPosts(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    PostPageResponse getPublishedPostsByUser(Long userId, boolean draft, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

}
