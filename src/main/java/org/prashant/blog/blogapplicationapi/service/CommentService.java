package org.prashant.blog.blogapplicationapi.service;

import org.prashant.blog.blogapplicationapi.payload.*;

public interface CommentService {
    CommentDto createComment(CreateCommentRequest createCommentRequest);
    CommentDto updateComment(UpdateCommentRequest updateCommentRequest);
    void deleteCommentById(DeleteCommentRequest deleteCommentRequest);
    void deleteCommentByUser(Long userId);
    void deleteCommentByPost(Long postId);
    CommentResponse getAllCommentsByPost(Long postId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    CommentResponse getAllCommentsByUser(Long userId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);


}
