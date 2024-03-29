package org.prashant.blog.blogapplicationapi.service;

import org.prashant.blog.blogapplicationapi.payload.CreatePostRequest;
import org.prashant.blog.blogapplicationapi.payload.PostDto;
import org.prashant.blog.blogapplicationapi.payload.PostResponse;
import org.prashant.blog.blogapplicationapi.payload.PostUpdateRequest;

import java.util.List;

public interface PostService {
    PostDto createPost(CreatePostRequest createPostRequest);
    PostDto updatePost(PostUpdateRequest postUpdateRequest);
    void deletePost(Long postId);
    PostResponse getPosts(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    PostDto getPostById(Long postId);
    PostResponse getPostsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    PostResponse getPostsByUser(Long userId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    PostResponse getPostsByTag(String tagName, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);

    PostResponse searchPosts(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
}
