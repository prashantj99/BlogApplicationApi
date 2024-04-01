package org.prashant.blog.blogapplicationapi.controllers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.prashant.blog.blogapplicationapi.entities.Post;
import org.prashant.blog.blogapplicationapi.payload.*;
import org.prashant.blog.blogapplicationapi.service.PostService;
import org.prashant.blog.blogapplicationapi.utils.AppConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class PostController {

    private final PostService postService;
    @PostMapping("/create_post/")
    public ResponseEntity<PostDto> createPost(@ModelAttribute CreatePostRequest createPostRequest) throws IOException {
        PostDto created_post = this.postService.createPost(createPostRequest);
        return new ResponseEntity<>(created_post, HttpStatus.CREATED);
    }

    @PutMapping("/update/")
    public ResponseEntity<PostDto> updatePost(@ModelAttribute PostUpdateRequest postUpdateRequest) throws IOException {
         PostDto updated_post = this.postService.updatePost(postUpdateRequest);
        return new ResponseEntity<>(updated_post, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<PostResponse> getAllPost(
            @RequestParam(value ="pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value ="pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.DEFAULT_POST_SORT_FIELD, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.DEFAULT_SORT_CRITERIA, required = false) String sortDir
    ){
        PostResponse postResponse= this.postService.getPosts(pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long postId){
        PostDto postDto = this.postService.getPostById(postId);
        return new ResponseEntity<>(postDto, HttpStatus.OK);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<PostResponse> getPostByCategory(
            @PathVariable Long categoryId,
            @RequestParam(value ="pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value ="pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.DEFAULT_POST_SORT_FIELD, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.DEFAULT_SORT_CRITERIA, required = false) String sortDir
    ){
         PostResponse postResponse= postService.getPostsByCategory(categoryId, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<PostResponse> getPostByUser(
            @PathVariable Long userId,
            @RequestParam(value ="pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value ="pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.DEFAULT_POST_SORT_FIELD, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.DEFAULT_SORT_CRITERIA, required = false) String sortDir
    ){
        System.out.println("debug post by user lin1");
        PostResponse postResponse= postService.getPostsByUser(userId, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<PostResponse> getPostByCategory(
            @PathVariable String keyword,
            @RequestParam(value ="pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value ="pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.DEFAULT_POST_SORT_FIELD, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.DEFAULT_SORT_CRITERIA, required = false) String sortDir
    ){
        PostResponse postResponse= postService.searchPosts(keyword, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    @GetMapping("/tag/{tagName}")
    public ResponseEntity<PostResponse> getPostByTagName(
            @PathVariable String tagName,
            @RequestParam(value ="pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value ="pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.DEFAULT_POST_SORT_FIELD, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.DEFAULT_SORT_CRITERIA, required = false) String sortDir
    ){
        PostResponse postResponse= postService.getPostsByTag(tagName, pageNumber, pageSize, sortBy, sortDir);
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{postId}/user/{userId}")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable Long postId, @PathVariable Long userId) throws IOException {
        DeletePostRequest deletePostRequest = new DeletePostRequest(postId, userId);
        this.postService.deletePost(deletePostRequest);
        return new ResponseEntity<>(new ApiResponse("post deleted successfully!!!", true), HttpStatus.OK);
    }

}
