package org.prashant.blog.blogapplicationapi.controllers;

import lombok.RequiredArgsConstructor;
import org.prashant.blog.blogapplicationapi.payload.*;
import org.prashant.blog.blogapplicationapi.service.CommentService;
import org.prashant.blog.blogapplicationapi.utils.AppConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/create")
    public ResponseEntity<CommentDto> createComment(@RequestBody CreateCommentRequest createCommentRequest) {
        CommentDto createdComment = commentService.createComment(createCommentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    @PutMapping("/update")
    public ResponseEntity<CommentDto> updateComment(@RequestBody UpdateCommentRequest updateCommentRequest) {
        CommentDto updatedComment = commentService.updateComment(updateCommentRequest);
        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{commentId}/user/{userId}")
    public ResponseEntity<String> deleteCommentById(@PathVariable Long commentId, @PathVariable Long userId) {
        DeleteCommentRequest deleteCommentRequest = new DeleteCommentRequest(commentId, userId);
        commentService.deleteCommentById(deleteCommentRequest);
        return ResponseEntity.ok("comment deleted successfully");
    }

    @DeleteMapping("/user/{userId}")
    public ResponseEntity<String> deleteCommentsByUser(@PathVariable Long userId) {
        commentService.deleteCommentByUser(userId);
        return ResponseEntity.ok("comment deleted successfully");
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<String> deleteCommentsByPost(@PathVariable Long postId) {
        commentService.deleteCommentByPost(postId);
        return ResponseEntity.ok("comment deleted successfully");
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<CommentPageResponse> getAllCommentsByPost(
            @PathVariable Long postId,
            @RequestParam(value ="pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value ="pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.DEFAULT_COMMENT_SORT_FIELD, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.DEFAULT_SORT_CRITERIA, required = false) String sortDir
    ) {
        CommentPageResponse commentPageResponse = commentService.getAllCommentsByPost(postId, pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(commentPageResponse);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<CommentPageResponse> getAllCommentsByUser(
            @PathVariable Long userId,
            @RequestParam(value ="pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value ="pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.DEFAULT_COMMENT_SORT_FIELD, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.DEFAULT_SORT_CRITERIA, required = false) String sortDir
    ) {
        CommentPageResponse commentPageResponse = commentService.getAllCommentsByUser(userId, pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.ok(commentPageResponse);
    }
}
