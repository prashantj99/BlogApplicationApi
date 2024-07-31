package org.prashant.blog.blogapplicationapi.controllers;

import lombok.RequiredArgsConstructor;
import org.prashant.blog.blogapplicationapi.entities.User;
import org.prashant.blog.blogapplicationapi.payload.*;
import org.prashant.blog.blogapplicationapi.service.PostService;
import org.prashant.blog.blogapplicationapi.utils.AppConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/post")
public class PostController {

    private final PostService postService;
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @PostMapping("/create")
    public ResponseEntity<?> createPost(@RequestBody CreatePostRequest createPostRequest) throws IOException {
        User login_user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PostDT created_post = this.postService.createPost(createPostRequest, login_user.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created_post);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updatePost(@RequestBody PostUpdateRequest postUpdateRequest) throws IOException {
        return ResponseEntity.ok(this.postService.updatePost(postUpdateRequest));
    }

    @GetMapping("/page")
    public ResponseEntity<PostPageResponse> getAllPost(
            @RequestParam(value ="pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value ="pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.DEFAULT_POST_SORT_FIELD, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.DEFAULT_SORT_CRITERIA, required = false) String sortDir
    ){
        PostPageResponse postPageResponse = this.postService.getPosts(pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(postPageResponse);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDT> getPostById(@PathVariable Long postId){
        PostDT post = this.postService.getPostById(postId);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<PostPageResponse> getPostByCategory(
            @PathVariable Long categoryId,
            @RequestParam(value ="pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value ="pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.DEFAULT_POST_SORT_FIELD, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.DEFAULT_SORT_CRITERIA, required = false) String sortDir
    ){
        PostPageResponse postPageResponse = postService.getPostsByCategory(categoryId, pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(postPageResponse);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<PostPageResponse> getPostByUser(
            @PathVariable Long userId,
            @RequestParam(value ="pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value ="pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.DEFAULT_POST_SORT_FIELD, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.DEFAULT_SORT_CRITERIA, required = false) String sortDir
    ){
        PostPageResponse postPageResponse = postService.getPostsByUser(userId, pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(postPageResponse);
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<PostPageResponse> getPostByCategory(
            @PathVariable String keyword,
            @RequestParam(value ="pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value ="pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.DEFAULT_POST_SORT_FIELD, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.DEFAULT_SORT_CRITERIA, required = false) String sortDir
    ){
        PostPageResponse postPageResponse = postService.searchPosts(keyword, pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(postPageResponse);
    }

    @GetMapping("/tag/{tagName}")
    public ResponseEntity<PostPageResponse> getPostByTagName(
            @PathVariable String tagName,
            @RequestParam(value ="pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value ="pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.DEFAULT_POST_SORT_FIELD, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.DEFAULT_SORT_CRITERIA, required = false) String sortDir
    ){
        PostPageResponse postPageResponse = postService.getPostsByTag(tagName, pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(postPageResponse);
    }

    @DeleteMapping("/{postId}/user/{userId}")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable Long postId, @PathVariable Long userId) throws IOException {
        DeletePostRequest deletePostRequest = new DeletePostRequest(postId, userId);
        this.postService.deletePost(deletePostRequest);
        return new ResponseEntity<>(new ApiResponse("post deleted successfully!!!", true), HttpStatus.OK);
    }

    @GetMapping("/published/{draft}/{userId}")
    public  ResponseEntity<?> publishedPostsByUserHandler(
            @PathVariable Long userId,
            @PathVariable Boolean draft,
            @RequestParam(value ="pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value ="pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.DEFAULT_POST_SORT_FIELD, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.DEFAULT_SORT_CRITERIA, required = false) String sortDir
    ){
        var posts = this.postService.getPublishedPostsByUser(userId, draft, pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/trending")
    public  ResponseEntity<?> trendingPostsHandler(
            @RequestParam(value ="pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value ="pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize
    ){
        var posts = this.postService.getTrendingPosts(pageNumber, pageSize);
        return ResponseEntity.ok(posts);
    }

}
