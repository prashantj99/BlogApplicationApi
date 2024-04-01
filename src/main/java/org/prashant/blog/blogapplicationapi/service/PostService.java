package org.prashant.blog.blogapplicationapi.service;

import org.prashant.blog.blogapplicationapi.payload.*;

import java.io.IOException;
import java.util.List;

public interface PostService {
    PostDto createPost(CreatePostRequest createPostRequest) throws IOException;
    PostDto updatePost(PostUpdateRequest postUpdateRequest) throws IOException;
    void deletePost(DeletePostRequest deletePostRequest) throws IOException;
    PostResponse getPosts(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    PostDto getPostById(Long postId);
    PostResponse getPostsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    PostResponse getPostsByUser(Long userId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    PostResponse getPostsByTag(String tagName, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    PostResponse searchPosts(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

}
