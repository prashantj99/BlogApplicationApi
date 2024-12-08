package org.prashant.blog.blogapplicationapi.controllers;

import lombok.RequiredArgsConstructor;
import org.prashant.blog.blogapplicationapi.entities.User;
import org.prashant.blog.blogapplicationapi.payload.*;
import org.prashant.blog.blogapplicationapi.service.PostService;
import org.prashant.blog.blogapplicationapi.service.UserService;
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
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(PostController.class);

    @PostMapping("/create")
    public ResponseEntity<?> createPostHandler(@RequestBody CreatePostRequest createPostRequest) throws IOException {
        User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PostDTO createdPost = this.postService.createPost(createPostRequest, loginUser.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updatePostHandler(@RequestBody PostUpdateRequest postUpdateRequest) throws IOException {
        return ResponseEntity.ok(this.postService.updatePost(postUpdateRequest));
    }

    @PutMapping("/{postId}/view")
    public ResponseEntity<?> incrementPostViewHandler(@PathVariable Long postId) {
        postService.incrementPostViews(postId);
        return ResponseEntity.ok(new ApiResponse("View count incremented successfully", true));
    }

    @GetMapping("/page")
    public ResponseEntity<PostPageResponse> fetchPostHanler(
            @RequestParam(value = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.DEFAULT_POST_SORT_FIELD, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.DEFAULT_SORT_CRITERIA, required = false) String sortDir
    ) {
        PostPageResponse postPageResponse = this.postService.getPosts(pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(postPageResponse);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostDTO> fetchPostHandler(@PathVariable Long postId) {
        PostDTO post = this.postService.getPostById(postId);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<PostPageResponse> fetchPostsByCategoryHanler(
            @PathVariable Long categoryId,
            @RequestParam(value = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.DEFAULT_POST_SORT_FIELD, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.DEFAULT_SORT_CRITERIA, required = false) String sortDir
    ) {
        PostPageResponse postPageResponse = postService.getPostsByCategory(categoryId, pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(postPageResponse);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<PostPageResponse> fetchPostsByUserHandler(
            @PathVariable Long userId,
            @RequestParam(value = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.DEFAULT_POST_SORT_FIELD, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.DEFAULT_SORT_CRITERIA, required = false) String sortDir
    ) {
        PostPageResponse postPageResponse = postService.getPostsByUser(userId, pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(postPageResponse);
    }

    @GetMapping("/search/{keyword}")
    public ResponseEntity<PostPageResponse> searchPostsHandler(
            @PathVariable String keyword,
            @RequestParam(value = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.DEFAULT_POST_SORT_FIELD, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.DEFAULT_SORT_CRITERIA, required = false) String sortDir
    ) {
        PostPageResponse postPageResponse = postService.searchPosts(keyword, pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(postPageResponse);
    }

    @GetMapping("/tag/{tagName}")
    public ResponseEntity<PostPageResponse> fetchPostsByTagNameHandler(
            @PathVariable String tagName,
            @RequestParam(value = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.DEFAULT_POST_SORT_FIELD, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.DEFAULT_SORT_CRITERIA, required = false) String sortDir
    ) {
        PostPageResponse postPageResponse = postService.getPostsByTag(tagName, pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(postPageResponse);
    }

    @DeleteMapping("/{postId}/user/{userId}")
    public ResponseEntity<?> deletePostHandler(@PathVariable Long postId, @PathVariable Long userId) throws IOException {
        this.postService.deletePost(postId, userId);
        return new ResponseEntity<>(new ApiResponse("Post deleted successfully!", true), HttpStatus.OK);
    }

    @GetMapping("/published/{userId}/{draft}")
    public ResponseEntity<?> fetchPublishedPostsByUserHandler(
            @PathVariable Long userId,
            @PathVariable Boolean draft,
            @RequestParam(value = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.DEFAULT_POST_SORT_FIELD, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.DEFAULT_SORT_CRITERIA, required = false) String sortDir
    ) {
        var posts = this.postService.getPublishedPostsByUser(userId, draft, pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/trending")
    public ResponseEntity<?> fetchTrendingPostsHandler(
            @RequestParam(value = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize
    ) {
        var posts = this.postService.getTrendingPosts(pageNumber, pageSize);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/recommended/category/{categoryId}")
    public ResponseEntity<?> fetchRecommendedPostsInCategoryHandler(
            @PathVariable Long categoryId,
            @RequestParam(value = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize
    ) {
        var posts = this.postService.getRecommendedPostsOfCategory(categoryId, pageNumber, pageSize);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/following")
    public ResponseEntity<PostPageResponse> getSubscribedPostsHandler(
            @RequestParam(value ="pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value ="pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.DEFAULT_POST_SORT_FIELD, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.DEFAULT_SORT_CRITERIA, required = false) String sortDir) {
        User loggedInUser = userService.getLoggedInUser().orElseThrow(()-> new RuntimeException("Unauthorized User!!!"));
        var posts = postService.getPostsFromSubscribedCategories(loggedInUser.getUserId(), pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(posts);
    }
}
