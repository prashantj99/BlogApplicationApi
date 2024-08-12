package org.prashant.blog.blogapplicationapi.service;

import org.prashant.blog.blogapplicationapi.payload.*;

public interface CommentService {
    CommentDTO createComment(CreateCommentRequest createCommentRequest);
    CommentDTO updateComment(UpdateCommentRequest updateCommentRequest);
    void deleteComment(Long commentId);
    CommentPageResponse getAllCommentsByPost(Long postId, Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    CommentPageResponse getAllCommentsByUser(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
}
